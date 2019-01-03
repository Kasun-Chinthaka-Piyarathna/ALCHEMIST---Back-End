package com.uom.it.alchemists.ureport;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasun Chinthaka Piyarathna
 */

/**
 * Class containing all services related to U REPORT
 */
@Path("/ureportservice")
public class UReportService {


    final static Logger LOGGER = Logger.getLogger(UReportService.class);

    /**
     * Test method to see whether web service is working.
     *
     * @param msg sent as a parameter
     * @return example if msg = hi -> Jersey say : hi
     */
    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String msg) {
        LOGGER.info("U Report Test Method is Called with param : " + msg);
        String output = "Jersey say : " + msg;
        return Response.status(200).entity(output).build();
    }

    @Path("/userSignUp")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response customerSignUp(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String email = queryParams.get("email").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String password = queryParams.get("password").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String name = queryParams.get("name").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String nearest_city = queryParams.get("nearest_city").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String mobile = queryParams.get("mobile").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String profile_image_url = queryParams.get("profile_image_url").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String cover_image_url = queryParams.get("cover_image_url").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        System.out.println(email);
        System.out.println(password);
        System.out.println(name);
        System.out.println(nearest_city);
        System.out.println(mobile);
        System.out.println(profile_image_url);
        System.out.println(cover_image_url);

        if (password.trim().length() > 0 && email.trim().length() > 0) {  //trim() returns a string with no leading or trailing white spaces
            if (!(password.trim().equals("undefined")) && !(email.trim().equals("undefined"))) {
                try {
                    DB db = DBConnection.getConnection();
                    DBCollection userCollection = db.getCollection("user");
                    BasicDBObject query = new BasicDBObject();
                    query.put("email", email);
                    DBCursor cursor = userCollection.find(query);
                    if (!(cursor.length() > 0)) {
                        BasicDBObject document = new BasicDBObject();
                        document.put("email", email);
                        document.put("password", password);
                        document.put("name", name);
                        document.put("nearest_city", nearest_city);
                        document.put("mobile", mobile);
                        document.put("profileImageUrl", profile_image_url);
                        document.put("coverImageUrl", cover_image_url);
                        userCollection.insert(document);


                        BasicDBObject whereQuery = new BasicDBObject();
                        List<BasicDBObject> upwd = new ArrayList<BasicDBObject>();
                        upwd.add(new BasicDBObject("email", email));
                        upwd.add(new BasicDBObject("password", password));
                        whereQuery.put("$and", upwd);

                        DBCursor dbCursor = userCollection.find(whereQuery);
                        String serialize = JSON.serialize(dbCursor);
                        return Response.status(200).entity(serialize).build();
                    } else {
                        String response = "Email Address already exists!";
                        return Response.status(200).entity(response).build();
                    }
                } catch (Exception e) {
                    String status = "404";
                    System.out.println(e);
                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }


    @Path("/updateUsername")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateUsername(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String name = queryParams.get("name").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && name.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(name.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("name", name);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";

                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }


    @Path("/updateNearestCity")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateNearestCity(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String nearest_city = queryParams.get("nearest_city").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && nearest_city.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(nearest_city.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("nearest_city", nearest_city);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    System.out.println(status);
                    return Response.status(200).entity(status).build();


                } catch (Exception e) {
                    String status = "404";
                    System.out.println(status);
                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        System.out.println(status);
        return Response.status(200).entity(status).build();
    }
    @Path("/updateMobile")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateMobile(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String mobile = queryParams.get("mobile").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && mobile.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(mobile.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("mobile", mobile);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";

                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }
    @Path("/updateProfileImageUrl")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateProfileImageUrl(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String profileImageUrl = queryParams.get("profileImageUrl").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && profileImageUrl.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(profileImageUrl.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("profileImageUrl", profileImageUrl);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";

                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }
    @Path("/updateCoverImageUrl")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateCoverImageUrl(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String updateCoverImageUrl = queryParams.get("updateCoverImageUrl").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && updateCoverImageUrl.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(updateCoverImageUrl.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("coverImageUrl", updateCoverImageUrl);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";

                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }


    @Path("/updatePassword")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePassword(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String password = queryParams.get("password").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 && password.trim().length() > 0) {
            if (!(user_id.trim().equals("undefined")) && !(password.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();

                    DBCollection rest = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));

                    BasicDBObject newDocument = new BasicDBObject();
                    newDocument.put("password", password);

                    BasicDBObject updateObj = new BasicDBObject();
                    updateObj.put("$set", newDocument);

                    rest.update(query, updateObj);

                    String status = "Updated";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";

                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println("404");
        return Response.status(200).entity(status).build();
    }


    @Path("/deleteUser")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteUser(@Context UriInfo ui) throws UnknownHostException {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        System.out.println(queryParams.toString());

        String user_id = queryParams.get("user_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (user_id.trim().length() > 0 ) {
            if (!(user_id.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();
                    DBCollection userCollection = db.getCollection("user");
                    BasicDBObject document = new BasicDBObject();
                    document.put("_id", new ObjectId(user_id));
                    // deleting the document
                    userCollection.remove(document);

                    String status = "deleted";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";
                    System.out.println(status);
                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        System.out.println(status);
        return Response.status(200).entity(status).build();
    }

    //login
    @Path("/userSignIn")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response customerSignIn(@Context UriInfo ui) throws UnknownHostException {

        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();


        String email = queryParams.get("email").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String password = queryParams.get("password").toString().replaceAll("\\[", "").replaceAll("\\]", "");

        if (email.trim().length() > 0 && password.trim().length() > 0) {  //trim() returns a string with no leading or trailing white spaces

            try {

                DB db = DBConnection.getConnection();
                DBCollection userCollection = db.getCollection("user");

                BasicDBObject whereQuery = new BasicDBObject();
                List<BasicDBObject> upwd = new ArrayList<BasicDBObject>();
                upwd.add(new BasicDBObject("email", email));
                upwd.add(new BasicDBObject("password", password));
                whereQuery.put("$and", upwd);

                DBCursor cursor = userCollection.find(whereQuery);
                if (cursor.length() > 0) {
                    String serialize = JSON.serialize(cursor);
                    return Response.status(200).entity(serialize).build();

                } else {
                    String status = "404";
                    String serialize = JSON.serialize(status);
                    return Response.status(200).entity(serialize).build();
                }

            } catch (Exception e) {
                String serialize = JSON.serialize("404");
                return Response.status(200).entity(serialize).build();
            }
        }

        String status = "404";
        String serialize = JSON.serialize(status);
        return Response.status(200).entity(serialize).build();
    }

    @Path("/createPost")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createNewPost(@Context UriInfo ui) throws UnknownHostException {

        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        System.out.println(queryParams.toString());

        String user_id = queryParams.get("UserId").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String post_text = queryParams.get("PostText").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String post_type = queryParams.get("PostType").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String file_url_old = queryParams.get("fileUrl").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        ;
        String file_url = file_url_old.replace("All_Image_Uploads/", "All_Image_Uploads%2F");
        file_url = file_url.replace("All_Video_Uploads/", "All_Video_Uploads%2F");

        if (user_id.trim().length() > 0 && post_text.trim().length() > 0 && post_type.trim().length() > 0 && file_url.trim().length() > 0) {  //trim() returns a string with no leading or trailing white spaces

            if (!(user_id.trim().equals("undefined") && post_text.trim().equals("undefined")) && !(post_type.trim().equals("undefined")) && !(file_url.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();
                    DBCollection userCollection = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));


                    BasicDBObject field = new BasicDBObject();
                    field.put("posts", 1);
                    DBCursor cursor = db.getCollection("user").find(query, field);
                    String post_id = user_id + "_p_" + 1;
                    try {
                        if (cursor.length() > 0) {
                            String serialize = JSON.serialize(cursor);
                            JSONArray jsonArray = new JSONArray(serialize);
                            System.out.println(serialize);
                            JSONObject firstObject = jsonArray.getJSONObject(0);
                            Object postKey = firstObject.get("posts");
                            System.out.println(postKey);
                            JSONArray jsonArray1 = new JSONArray(postKey.toString());
                            post_id = user_id + "_p_" + String.valueOf(jsonArray1.length() + 1);
                            System.out.println(post_id);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    System.out.println(Constants.sdf.format(timestamp));


                    BasicDBObject cd = new BasicDBObject();
                    cd.put("post_id", post_id);
                    cd.put("post_text", post_text);
                    cd.put("post_type", post_type);
                    cd.put("file_url", file_url);
                    cd.put("time_stamp", Constants.sdf.format(timestamp));

                    System.out.println(file_url);


                    BasicDBObject update = new BasicDBObject();
                    update.put("$push", new BasicDBObject("posts", cd));

                    userCollection.update(query, update, true, true);

                    String status = "Successful";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    String status = "404";
                    return Response.status(200).entity(status).build();
                }
            }
        }
        String status = "404";
        return Response.status(200).entity(status).build();
    }

    @Path("/createComment")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createComment(@Context UriInfo ui) throws UnknownHostException {

        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();


        System.out.println(queryParams.get("comment").toString());
        String user_id = queryParams.get("UserId").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String post_id = queryParams.get("PostId").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        String comment = queryParams.get("comment").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        //  String time_stamp = queryParams.get("TimeStamp").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        System.out.println(comment);
        if (user_id.trim().length() > 0 && post_id.trim().length() > 0 && comment.trim().length() > 0) {  //trim() returns a string with no leading or trailing white spaces

            if (!(user_id.trim().equals("undefined") && post_id.trim().equals("undefined")) && !(comment.trim().equals("undefined"))) {

                try {

                    DB db = DBConnection.getConnection();
                    DBCollection userCollection = db.getCollection("user");

                    BasicDBObject query = new BasicDBObject();
                    query.put("_id", new ObjectId(user_id));


                    BasicDBObject field = new BasicDBObject();
                    field.put("comments", 1);
                    DBCursor cursor = db.getCollection("user").find(query, field);
                    String comment_id = user_id + "_c_" + 1;
                    try {
                        if (cursor.length() > 0) {
                            String serialize = JSON.serialize(cursor);
                            JSONArray jsonArray = new JSONArray(serialize);
                            System.out.println(serialize);
                            JSONObject firstObject = jsonArray.getJSONObject(0);
                            Object commentKey = firstObject.get("comments");
                            System.out.println(commentKey);
                            JSONArray jsonArray1 = new JSONArray(commentKey.toString());
                            comment_id = user_id + "_c_" + String.valueOf(jsonArray1.length() + 1);
                            System.out.println(comment_id);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    System.out.println(Constants.sdf.format(timestamp));

                    BasicDBObject document = new BasicDBObject();
                    document.put("comment_id", comment_id);
                    document.put("post_id", post_id);
                    document.put("comment", comment);
                    document.put("time_stamp", Constants.sdf.format(timestamp));

                    BasicDBObject update = new BasicDBObject();
                    update.put("$push", new BasicDBObject("comments", document));

                    userCollection.update(query, update, true, true);


                    String status = "Successful";
                    return Response.status(200).entity(status).build();

                } catch (Exception e) {
                    System.out.println(comment);
                    String status = "404";
                    return Response.status(200).entity(status).build();
                }
            }
        }
        System.out.println(comment);
        String status = "404";
        return Response.status(200).entity(status).build();
    }


    //getting all news feed
    @Path("/newsfeed")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getNewsFeed(@Context UriInfo ui) throws UnknownHostException {

        String serialize;
        try {

            DB db = DBConnection.getConnection();
            DBCollection userCollection = db.getCollection("user");
            DBCursor cursor = userCollection.find();
            serialize = JSON.serialize(cursor);

        } catch (Exception e) {
            String status = "404";
            return Response.status(200).entity(status).build();
        }
        return Response.status(200).entity(serialize).build();
    }

    //getting all comments for a particular post
    @Path("/getAllComments")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAllComments(@Context UriInfo ui) throws UnknownHostException {

        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();


        String post_id = queryParams.get("post_id").toString().replaceAll("\\[", "").replaceAll("\\]", "");


        if (post_id.trim().length() > 0) {  //trim() returns a string with no leading or trailing white spaces

            try {

                DB db = DBConnection.getConnection();

                DBCollection userCollection = db.getCollection("user");


                DBCursor cur = userCollection.find();
                JSONArray commentArray = new JSONArray();


                for (DBObject doc : cur) {
                    String userName = (String) doc.get("name");
                    String profileImageUrl = (String) doc.get("profileImageUrl");
                    System.out.println(userName);

                    try {
                        BasicDBList commentsDBObject = (BasicDBList) doc.get("comments");
                        if (commentsDBObject.size() > 0) {
                            for (int i = 0; i < commentsDBObject.size(); i++) {
                                DBObject dbObject = (DBObject) commentsDBObject.get(i);
                                String post_index = dbObject.get("post_id").toString();
                                if (post_index.equals(post_id)) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("comment", dbObject.get("comment").toString());
                                    jsonObject.put("time_stamp", dbObject.get("time_stamp").toString());
                                    jsonObject.put("user_name", userName);
                                    jsonObject.put("profile_img_url", profileImageUrl);
                                    commentArray.put(jsonObject);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }

                }

                String serialize = commentArray.toString();
                return Response.status(200).entity(serialize).build();

            } catch (Exception e) {
                String status = "404";

                return Response.status(200).entity(status).build();
            }


        }

        String status = "404";

        return Response.status(200).entity(status).build();


    }

    //getting all notifications
    @Path("/getAllNotifications")
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAllNotifications(@Context UriInfo ui) throws UnknownHostException {

        try {

            DB db = DBConnection.getConnection();
            DBCollection userCollection = db.getCollection("user");

            DBCursor cur = userCollection.find();
            JSONArray notificationsArray = new JSONArray();


            for (DBObject doc : cur) {
                String userName = (String) doc.get("name");
                String profileImageUrl = (String) doc.get("profileImageUrl");

                try {
                    BasicDBList commentsDBObject = (BasicDBList) doc.get("comments");
                    BasicDBList postsDBObject = (BasicDBList) doc.get("posts");
                    if (commentsDBObject.size() > 0) {
                        for (int i = 0; i < commentsDBObject.size(); i++) {
                            DBObject dbObject = (DBObject) commentsDBObject.get(i);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "comment");
                            jsonObject.put("time_stamp", dbObject.get("time_stamp").toString());
                            jsonObject.put("user_name", userName);
                            jsonObject.put("profile_img_url", profileImageUrl);
                            notificationsArray.put(jsonObject);

                        }
                    }
                    if (postsDBObject.size() > 0) {
                        for (int i = 0; i < postsDBObject.size(); i++) {
                            DBObject dbObject = (DBObject) postsDBObject.get(i);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "post");
                            jsonObject.put("time_stamp", dbObject.get("time_stamp").toString());
                            jsonObject.put("user_name", userName);
                            jsonObject.put("profile_img_url", profileImageUrl);
                            notificationsArray.put(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }

            String serialize = notificationsArray.toString();
            return Response.status(200).entity(serialize).build();

        } catch (Exception e) {
            String status = "404";

            return Response.status(200).entity(status).build();
        }

    }


    //Still use testing purposes
    public static void main(String[] args) throws UnknownHostException {


    }
}