package com.notes.signalServer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.util.StringUtils;

@RestController
//@RequestMapping("/share")
public class SignalServerController {

	
    ArrayList<UserModel> users = new ArrayList<UserModel>();
    
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    public String receiveMessage(@Payload String message){
        return "HEllo Bagga";
    }
    

	@MessageMapping("/offer")
	public void Offer(@Payload String payload) {
	    try {
	        System.out.println("Offer Came");
	        UserModel offer = mapper.readValue(payload, UserModel.class);
	        // Perform any additional processing or send the offer to other clients
	        System.out.println("Offer Sent");
	        UserModel user = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(offer.getUserName())).findFirst().orElse(null);
			  if(user != null) {
				  user.setOffer(offer.getOffer());
				  user.setPassword(offer.getPassword());
				  System.out.println(user.getPassword());
			  }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@MessageMapping("/get-candidate")
	public UserModel getCandidate(@Payload String payload) {
		
		UserModel sender = null;
		try {
	        UserModel offer = mapper.readValue(payload, UserModel.class);
			sender = users.stream().filter(x -> x.getPassword() !=null && x.getPassword().equalsIgnoreCase(offer.getPassword())).findFirst().orElseGet(null);
			simpMessagingTemplate.convertAndSendToUser(offer.getUserName(), "/message", sender);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sender;
	}
	
	@MessageMapping("/new-client")
	public void newClient(@Payload String payload) {
	    try {
	        UserModel newUser = mapper.readValue(payload, UserModel.class);
	        UserModel user = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(newUser.getUserName())).findFirst().orElse(null);

	        if (user == null) {
	            System.out.println("New client added: " + newUser.getUserName());
	            users.add(newUser);
	        } else {
	            System.out.println("Username or password already exists: " + newUser.getUserName());
	        }
	    } catch (Exception e) {
	        System.err.println("Error processing new client request: " + e.getMessage());
	        // Optionally, send an error response back to the client
	    }
	}

	
	@MessageMapping("/sendIceCandidateToSignalingServer")
	public void newIceCandidate(@Payload String payload) {
		try {
			System.out.println("new-ice-candiadate");
			UserModel newIceCandidate = mapper.readValue(payload, UserModel.class);
			if(newIceCandidate.isDidIOffer()) {
				UserModel offerInOffers = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(newIceCandidate.getUserName())).findFirst().orElse(null);
				if(offerInOffers != null) {
					offerInOffers.setIceCandidate(newIceCandidate.getIceCandidate());
					if(!StringUtils.isEmpty(offerInOffers.getAnswererUserName()) ) {
						UserModel socketToSendTo = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(offerInOffers.getAnswererUserName())).findFirst().orElse(null);
						if(socketToSendTo != null){
							simpMessagingTemplate.convertAndSendToUser(socketToSendTo.getUserName(), "/receivedIceCandidateFromServer",newIceCandidate.getIceCandidate() );
	                    }else{
	                        System.out.println("Ice candidate recieved but could not find answere");
	                    }
					}
				}
			}else {
				UserModel offerInOffers = users.stream().filter(x ->x.getAnswererUserName() != null &&  x.getAnswererUserName().equalsIgnoreCase(newIceCandidate.getUserName())).findFirst().orElse(null);
//				UserModel socketToSendTo = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(offerInOffers.getAnswererUserName())).findFirst().orElse(null);
				if(offerInOffers != null){
					simpMessagingTemplate.convertAndSendToUser(offerInOffers.getUserName(), "/receivedIceCandidateFromServer",newIceCandidate.getIceCandidate() );
	            }else{
	            	System.out.println("Ice candidate recieved but could not find offerer");
	            }
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@MessageMapping("/newAnswer")
	public void newAnswer(@Payload String payload) {
		try {
			UserModel offerWithAnswer = mapper.readValue(payload, UserModel.class);
	        UserModel socketToAnswer = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(offerWithAnswer.getUserName())).findFirst().orElse(null);
	        if(socketToAnswer!= null) {
		        String socketNameToAnswer = socketToAnswer.getUserName();
		        socketToAnswer.setAnswer(offerWithAnswer.getAnswer());
		       	socketToAnswer.setAnswererUserName(offerWithAnswer.getAnswererUserName());
		       	//send ice candidate to client 2
				simpMessagingTemplate.convertAndSendToUser(offerWithAnswer.getAnswererUserName(), "/receivedIceCandidateFromServer",socketToAnswer.getIceCandidate() );
				//send updated offer to client 1
				simpMessagingTemplate.convertAndSendToUser(socketNameToAnswer, "/answerResponse",socketToAnswer.getAnswer() );

	        }
	        

	        
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	@MessageMapping("/removeUser")
	public void removeUserFromList(@Payload String userName) {
		
		UserModel sender = null;
		try {
			sender = users.stream().filter(x -> x.getUserName().equalsIgnoreCase(userName)).findFirst().orElse(null);
			if(sender != null) users.remove(sender);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	//    @MessageMapping("/private-message")
//    public String recMessage(@Payload String message){
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
//        System.out.println(message.toString());
//        return message;
//    }
}
