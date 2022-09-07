package com.story.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stories")
public class CreateStoryModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int story_id;
	private String story_name;
	private int author_id;
	private String img_link;
	private String story_category;
	private String story_desc;
	private int view_count = 0;
	private Date published_date;
	
	public CreateStoryModel() {
		super();
		 
	}
	
	
	

	public CreateStoryModel(int story_id, String story_name, int author_id, String img_link, String story_category,
			String story_desc, int view_count, Date published_date) {
		super();
		this.story_id = story_id;
		this.story_name = story_name;
		this.author_id = author_id;
		this.img_link = img_link;
		this.story_category = story_category;
		this.story_desc = story_desc;
		this.view_count = view_count;
		this.published_date = published_date;
	}




	public int getStory_id() {
		return story_id;
	}

	public void setStory_id(int story_id) {
		this.story_id = story_id;
	}

	public String getStory_name() {
		return story_name;
	}

	public void setStory_name(String story_name) {
		this.story_name = story_name;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getImg_link() {
		return img_link;
	}

	public void setImg_link(String img_link) {
		this.img_link = img_link;
	}

	public String getStory_category() {
		return story_category;
	}

	public void setStory_category(String story_category) {
		this.story_category = story_category;
	}

	public String getStory_desc() {
		return story_desc;
	}

	public void setStory_desc(String story_desc) {
		this.story_desc = story_desc;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public Date getPublished_date() {
		return published_date;
	}

	public void setPublished_date(Date published_date) {
		this.published_date = published_date;
	}

	@Override
	public String toString() {
		return "CreateStoryModel [story_id=" + story_id + ", story_name=" + story_name + ", author_id=" + author_id
				+ ", img_link=" + img_link + ", story_category=" + story_category + ", story_desc=" + story_desc
				+ ", view_count=" + view_count + ", published_date=" + published_date + "]";
	}
	
	
	
	 
	
	
	
	
	
	

}
