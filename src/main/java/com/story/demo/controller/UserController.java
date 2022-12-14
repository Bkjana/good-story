package com.story.demo.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.story.demo.logic.StoryAction;
import com.story.demo.model.ApprovedStory;
import com.story.demo.model.UserModel;
import com.story.demo.repository.ApprovedStoryRepo;
import com.story.demo.services.UserService;

@Controller
public class UserController {
	@Autowired
  	UserService userService;
	
	@Autowired
	ApprovedStoryRepo approvedStoryRepo;
 
	StoryAction storyAction=new StoryAction();
	
	String checkForRedirectPost="";
  
	
	UserModel obj;
	
	ArrayList<ApprovedStory> stories;
	ArrayList<ApprovedStory> Drama;
	ArrayList<ApprovedStory> Comedy;
	ArrayList<ApprovedStory> Sci_fi;
	ArrayList<ApprovedStory> Horror;
	ArrayList<ApprovedStory> Tragedy;
	ArrayList<ApprovedStory> Romantic;
	
	
	@RequestMapping("/")
	public ModelAndView myHome(HttpSession session) {
		stories=approvedStoryRepo.findAllOrderByView();
		Drama = approvedStoryRepo.findStoryOfCatagory("Drama");
		Comedy = approvedStoryRepo.findStoryOfCatagory("Comedy");
		Sci_fi = approvedStoryRepo.findStoryOfCatagory("Sci-fi");
		Horror = approvedStoryRepo.findStoryOfCatagory("Horror");
		Tragedy = approvedStoryRepo.findStoryOfCatagory("Tragedy");
		Romantic = approvedStoryRepo.findStoryOfCatagory("Romantic");
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("Drama",Drama);
		mv.addObject("Comedy",Comedy);
		mv.addObject("Sci_fi",Sci_fi);
		mv.addObject("Horror",Horror);
		mv.addObject("Tragedy",Tragedy);
		mv.addObject("Drama",Drama);
		mv.addObject("Romantic",Romantic);
		session.setAttribute("stories", stories);
	
		return mv;
	}
	
  
	@RequestMapping("/story-{author_id}-{story_id}")
	public ModelAndView myStory(@PathVariable("author_id") int author_id,@PathVariable("story_id") int story_id ,HttpSession session) throws IOException{
		if(session.getAttribute("usermsg")!=null) {
			String storyFile=storyAction.getStory(author_id, story_id);
			if(storyFile=="") {
				System.err.println("file empty");
				ModelAndView modelAndView=new ModelAndView("404");
				return modelAndView;
			}
			ModelAndView modelAndView=new ModelAndView("single-post");
			modelAndView.addObject("storyFile", storyFile);
			ApprovedStory storyInfo=approvedStoryRepo.getReferenceById(story_id);
			int view=storyInfo.getView_count();
			view++;
			approvedStoryRepo.incrementViewCount(story_id, view);
			modelAndView.addObject("storyInfo", storyInfo);
			return modelAndView;
		}
		else {
			ModelAndView modelAndView=new ModelAndView("login");
			checkForRedirectPost="story-"+author_id+"-"+story_id;
			return modelAndView;
		}
	}
	
	
	@RequestMapping("/category/{catego}")
	public ModelAndView category(@PathVariable String catego,HttpSession session) {
		if(session.getAttribute("usermsg") != null) {
			ModelAndView modelAndView=new ModelAndView("category");
			ArrayList<ApprovedStory> story=approvedStoryRepo.findStoryOfCatagory(catego);;
			modelAndView.addObject("category", story);
			return modelAndView;
		}
		else {
			checkForRedirectPost="category/"+catego;
			return new ModelAndView("redirect:/userLogin");
		}
	}
	
	
   @RequestMapping("/userLogin")
	public String login() {
		return "login";
	}
   
   @PostMapping("/userLogin")
    public String Userlogin(String useremail,String userpassword,HttpSession session) {
	   if(userService.checkingEmailPass(useremail, userpassword) == null) {		   
			System.out.println("Login Un-Sucessfull..");
			session.setAttribute("usermsgWrongPass", "Please Provide Registered Email Id And Password");
			return "redirect:/userLogin";
		}
		else {
			System.out.println("Login Sucessfull..");
			obj = userService.checkingEmailPass(useremail, userpassword);
			session.setAttribute("usermsg", obj);
			if(checkForRedirectPost=="")
				return "redirect:/";
			else
				return "redirect:/"+checkForRedirectPost;
		}
   }
	
	@RequestMapping("/userSignup")
	public String signup() {	
		return "signup";
	}
	
	 @PostMapping("/userSignup")
	   public String UserSignup(@ModelAttribute UserModel info,HttpSession session) {
		   if (!info.getUserpassword().equals(info.getUsercpaasword())) {
			   session.setAttribute("userMassage", "Password Mismatch");
			return "redirect:/userSignup";
		 }
		   else if(userService.checkDuplicateEmail(info.getUseremail()) != null) {
			   session.setAttribute("userMassage", "Email Already Exist");
				return "redirect:/userSignup";
		   }
		 else{
			   System.out.println(info); 
			   userService.userSave(info);
			   session.setAttribute("userLoginMassage", "Registered Successful..Login Here");
			return "redirect:/userLogin"; 
		   }
		     
	   }
	 
	 @RequestMapping("/userprofile")
		public ModelAndView userprofile(HttpSession session) {
			if(session.getAttribute("usermsg") != null) {
				String folder="D:\\SpringBoot\\story\\story-master\\src\\main\\resources\\static\\userimage\\";
				String img="/userimage/";
				Path path = Paths.get(folder+obj.getId()+".jpeg");
				if(Files.exists(path) && !Files.isDirectory(path)) {
			      img=img+obj.getId()+".jpeg";
				}
				else {
					img+="avatar7.png";
				}
				ModelAndView modelAndView=new ModelAndView("userprofile");
				modelAndView.addObject("img", img);
				return modelAndView;
			}
			else{
				checkForRedirectPost="userprofile";
				return new ModelAndView("redirect:/userLogin");
			}
		}
	 @RequestMapping("/userLogout")
	 public String userLogout(HttpSession session) {
		 if(session.getAttribute("usermsg")!=null) {
				session.removeAttribute("usermsg");
		 }
		 return "redirect:/";	
	 }
	 
	 @RequestMapping("/userImage")
	 public String userImageChange(@RequestParam("userImg") MultipartFile file,HttpSession session) throws IOException {
		 if(session.getAttribute("usermsg") != null) {
			 String folder="D:\\SpringBoot\\story\\story-master\\src\\main\\resources\\static\\userimage\\";
			 byte[] bytes=file.getBytes();
			 Path path=Paths.get(folder+obj.getId()+".jpeg");
			 Files.write(path, bytes);
			 return "redirect:/userprofile";
		 }
		 else{
			return "redirect:/userLogin";
		 }
	 }
	 
	 @RequestMapping("/*")
	 public String handleWhiteLevelError() {
		 return "404";
	 }
}
