# CrimsonCode

## Introduction

Crimson Code is a REST-APIs for a web application platform dedicated to providing high-quality, reliable content for tech enthusiasts. Whether you're a developer, IT professional, student, or hobbyist, TechTales offers a centralized space where accurate, vetted information is at your fingertips. Our mission is to foster a community built on trust and shared knowledge, ensuring that every post and comment contributes to a smoother and more enjoyable tech journey


- **Authors:**
	 - Mohamed Ali [LinkedIn](https://www.linkedin.com/in/mohamed-ali7/) | [GitHub](https://github.com/Mohamed-Ali7)


## Installation

To run CrimsonCode locally, follow these steps:

1. **Clone the repository:**
   ```
   git https://github.com/Mohamed-Ali7/crimson_code_blog.git` 

2.  **Set up the database:**
    
    -   Use the provided SQL file `crimson_code_setup.sql` to set up your MySQL database schema, tables, and user.

3.  **Configure the email service:**

	In this project I am using MailHog as my **SMTP** Server.
	MailHog is an email-testing tool with a fake SMTP server underneath.
	You can find more information about MailHog here: [MailHog](https://mailtrap.io/blog/mailhog-explained/)
    
 - Open `crimson-code-blog-rest-apis/src/main/resources/application.properties` and locate the following properties:
 
```
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=
spring.mail.properties.mail.smtp.starttls.enable=
```

- Replace the values of these properties with your own specific info.

    -   For example, you can configure the application to use a different email server like **Gmail** by updating the following line: 
		 ```
		 spring.mail.host=smtp.gmail.com 
		 ```
	 - Ensure that the necessary email configurations (server, port, TLS/SSL settings) are set according to your email provider's specifications.
        
4.  **Run the Spring boot REST-APIs application:**
    
    Run you spring boot application as **Java application** with the help of the IDE you are using 
    
	This will launch the REST-APIs application on your **localhost** using the port **8080**, making it accessible for testing and development purposes. Ensure that the database is configured correctly before running the application.


## Usage
    
Once the app is up and running, you can access the APIs using **Postman** or any other **API platform (client)** you wish by using the following URL as the  base address `http://localhost:8080/`.


## API Endpoints

**Don't forget to prefix the following endpoints with this URL `http://localhost:8080`**

### Authentication Endpoints

- **Register:** `/api/auth/register`
	- **Description:** Registers a new user and sends an email verification.
	- **Method:** POST
	- **Protection:** Non-Protected
	- **Request Body example:**
		![register_request](https://github.com/user-attachments/assets/7637c828-ea10-492e-beae-1f1ca9febbb1)
    	- **Response Body:**
       		![register_response](https://github.com/user-attachments/assets/3bf832c2-c9d7-4777-aa28-06fea1d6c353)


- **Login:** `/api/auth/login`
	- **Description:** Authenticates user credentials, verifies email, and provides JWT tokens.
	- **Method:** POST 
	- **Protection:** Non-Protected
	- **Request Body example:**
        ![loging_request](https://github.com/user-attachments/assets/b8d2d956-e6ef-4b08-adc8-a88862aeb393)
    	- **Response Body:**
        	- if user's email is verified
            		![login_response_if_email_verified](https://github.com/user-attachments/assets/bc48dc50-0248-4e9e-9963-e92ed329b501)
        	- if user's email is not verified
            		![login_response_if_email_unverified](https://github.com/user-attachments/assets/edbbf71e-6c32-42a8-9bb3-055b172ac3f2)

	
- **Refresh:** `/api/auth/refresh`
	- **Description:** Refreshes the user's access token when it has expired.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![refresh_token_response](https://github.com/user-attachments/assets/12ef38ce-21fd-4b50-86df-2199a6d238d4)

- **Logout:** `/api/auth/logout`
	- **Description:** Logs the user out.
	- **Method:** POST
	- **Protection:** Protected
    	- **Request Body example:**
        	![logout_request](https://github.com/user-attachments/assets/4670effd-cee4-4152-bd9b-bc8e3dda58d8)
    	- **Response Body:**
        	![logout_response](https://github.com/user-attachments/assets/f76962b4-784e-4575-b71d-10ef8622180c)


- **Email Verification:** `/api/auth/email-verification/<token>`
	- **Description:** Verifies the user's email address.
	- **Method:** GET 
	- **Protection:** Non-Protected
	

- **Resend Verification Email:** `/api/auth/email-verification-request`
	- **Description:** Sends a new email verification to the user's registered email address.
	- **Method:** POST
	- **Protection:** Non-Protected
    	- **Request Body example:**
        	![email_verification_request](https://github.com/user-attachments/assets/9e6e0707-e8bd-4eb5-bb70-29c93923a202)
    	- **Response Body:**
        	- if email already verified 
            		![email_verification_response_if_email_already_verified](https://github.com/user-attachments/assets/6a385615-565d-4bf8-8c16-59620a008f7f)

        	- if email is not verified
            		![email_verification_response](https://github.com/user-attachments/assets/b2e1f0d3-5a58-4faf-8778-78f960c160c3)

### User Endpoints
- **Get All Users:** `/api/users`
	- **Description:** Retrieves a list of all registered users. Accessible only by admin users.
	- **Method:** GET
	- **Protection:** Protected (Admin Only)
    	- **Response Body Example:**
        	![Get_all_users_response](https://github.com/user-attachments/assets/c9c77fd1-524a-4f7a-b9a5-3e096f4475e3)


- **Get Specific User:** `/api/users/{publicId}`
	- **Description:** Retrieves information about a specific user.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body Example:**
        	![Get_user_response](https://github.com/user-attachments/assets/6c6668e4-e53f-458a-bdb4-634de2e8714b)


- **Update Specific User:** `/api/users/{publicId}`
	- **Description:** Updates the user's information. Users can only update their own data.
	- **Method:** PUT
	- **Protection:** Protected
    	- **Request Body example:**
        	![update_user_request](https://github.com/user-attachments/assets/7eb8cbdc-676b-4027-a46e-aaa729d6364f)
    	- **Response Body:**
        	![Update_user_response](https://github.com/user-attachments/assets/bcd1ada8-6250-4f24-aaeb-b00bbcd4a32b)

- **Delete Specific User:** `/api/users/{publicId}`
	- **Description:** Deletes a user account. Users can only delete their own account.
	- **Method:** DELETE
	- **Protection:** Protected
    	- **Response Body:**
        	![Delete_user_response](https://github.com/user-attachments/assets/16a9b045-164e-416b-81af-d43abcdc684a)


- **Get User's Posts:** `/api/users/{publicId}/posts`
	- **Description:** Retrieves all posts created by a specific user.
	- **Method:** GET 
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![Get_user_posts](https://github.com/user-attachments/assets/1d079e32-7860-4367-8981-d220e205fec4)


- **Change User's Password:** `/api/users/{publicId}/edit/password`
	- **Description:** Change the password for the current user.
	- **Method:** PUT 
	- **Protection:** Protected
	- **Request Body example:**
    		![Change_user_password_request](https://github.com/user-attachments/assets/96b34813-049a-4a75-a37b-0b034f52c316)
    	- **Response Body:**
        	![Change_user_password_response](https://github.com/user-attachments/assets/04c25ede-6d15-4f0f-9fbc-751eaf0b4242)

### Post Endpoints

- **Create Post:** `/api/posts` 
	- **Description:** Creates a new post. Only authenticated users can create posts
	- **Method:** POST
	- **Protection:** Protected
	- **Request Body example:**
		![create_post_request](https://github.com/user-attachments/assets/729374df-a0d8-4691-a5ae-4a1e749bec94)
    	- **Response Body:**
        	![create_post_response](https://github.com/user-attachments/assets/9d9fe331-aed7-4b2f-aad4-e09e1be7f5b0)


- **Get All Posts:** `/api/posts`
	- **Description:** Retrieves a list of all posts.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![Get_user_posts](https://github.com/user-attachments/assets/53492e7d-4533-4f40-9f78-3ba50c8419bf)

- **Get Specific Post:** `/api/posts/{id}`
	- **Description:** Retrieves details of a specific post.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![get_post_response](https://github.com/user-attachments/assets/bedfb312-7c93-4368-8811-3c70904d98c3)

- **Update Specific Post:** `/api/posts/{id}`
	- **Description:** Updates a specific post. Only the post author can update the post.
	- **Method:** PUT
	- **Protection:** Protected
    	- **Request Body example:**
        	![update_post_request](https://github.com/user-attachments/assets/7f29026d-fdd5-4ffd-923a-0edc1c65dc6d)
    	- **Response Body:**
        	![update_post_response](https://github.com/user-attachments/assets/8178fd21-7455-4104-9e12-77e2fd69b5af)

- **Delete Specific Post:** `/api/posts/{id}`
	- **Description:** Deletes a specific post. Only the post author can delete the post. 
	- **Method:** DELETE
	- **Protection:** Protected
    	- **Response Body:**
        	![delete_post_response](https://github.com/user-attachments/assets/21b1f456-01a3-4b2f-aab1-eed247bc221b)

### Comment Endpoints
- **Create Comment:** `/api/posts/{post_id}/comments`
	- **Description:** Creates a new comment for a specific post. Only authenticated users can create comments.
	- **Method:** POST
	- **Protection:** Protected
    	- **Request Body example:**
        	![create_comment_request](https://github.com/user-attachments/assets/4cdf63fe-05cf-42e1-acc3-4a8d7508fdef)
    	- **Response Body:**
        	![create_comment_response](https://github.com/user-attachments/assets/9649039c-180b-42b5-b95a-827de855171f)

- **Get All Comments:** `/api/posts/{post_id}/comments`
	- **Description:** Retrieves all comments for a specific post.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![get_post_comments_response](https://github.com/user-attachments/assets/76116fe2-a9a2-4f8c-a38f-43d0de82355d)

- **Get Specific Comment:** `/api/posts/{post_id}/comments/{comment_id}`
	- **Description:** Retrieves details of a specific comment on a specific post.
	- **Method:** GET
	- **Protection:** Non-Protected
    	- **Response Body:**
        	![create_comment_response](https://github.com/user-attachments/assets/3fb5896d-17fb-4e91-9ef0-2c83e3411e15)

- **Update Specific Comment:** `/api/posts/{post_id}/comments/{comment_id}`
	- **Description:** Updates a specific comment. Only the comment author can update the comment.
	- **Method:** PUT
	- **Protection:** Protected
    	- **Request Body example:**
        	![update_comment_request](https://github.com/user-attachments/assets/0bc2733e-eaa5-4925-8eb2-697349e4b2c2)
    	- **Response Body:**
        	![update_comment_response](https://github.com/user-attachments/assets/22fe664b-d32a-4bab-888f-702f3a9eb981)

- **Delete Specific Comment:** `/api/posts/{post_id}/comments/{comment_id}`
	- **Description:** Deletes a specific comment. Only the comment author can delete the comment.
	- **Method:** DELETE
	- **Protection:** Protected
    	- **Response Body:**
        	![delete_comment_response](https://github.com/user-attachments/assets/223c5bd2-9d22-416c-a6ac-cd72d2ad9e12)

