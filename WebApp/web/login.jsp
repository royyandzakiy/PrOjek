<%-- 
    Document   : login
    Created on : Nov 8, 2017, 12:43:29 AM
    Author     : Zack
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="org.json.simple.parser.*"%>
<%@page import="pack.RESTSend"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--<!DOCTYPE html>

<?php  //Start the Session
  session_start();
  require('blog_connect.php');

  if (isset($_POST['username']) and isset($_POST['password'])){
    $username = $_POST['username'];
    $password = $_POST['password'];
    //mengecek apakah data ada di database
    
    /*CONNECT DB*/
    sql_connect('gojek_db');

    // tahap 1
    $query_count = "SELECT COUNT(*) FROM pengguna WHERE username='$username' and pass='$password'";
    $result_count = $con->query($query_count);

    $query = "SELECT id, nama FROM pengguna WHERE username='$username' and pass='$password'";
    $result = $con->query($query);

    if ($row_count = $result_count->fetch(PDO::FETCH_NUM)) {
      if($row_count[0] > 0) {
        
        if ($row = $result->fetch(PDO::FETCH_NUM)) {
          $_SESSION['id_pengguna'] = $row[0];
          $_SESSION['username'] = $username;
          $_SESSION['nama'] = $row[1];
          $_SESSION['is_logged'] = TRUE;
        }        
      } else {
        echo "Invalid Login Credentials.";
        header('location:login.php');
      }
    }
  }

  $query_count = null;
  $result_count = null;
  $con = null;
  
  //pesan yang disampaikan ketika user login
  if (isset($_SESSION['username'])){
    header("Location: index.php?username=". $username);
  } else {
?>-->

<html>
    <%
        /* Check the request */
        if (request.getParameter("submit") != null){
//            out.print("<script>alert('debug');</script>");
            try{
                /* Consuming API using GET method */
                String user = request.getParameter("username");
                String pass = request.getParameter("password");
                String urlParameter = "username="+user+"&password="+pass;
                String urlRequest = "http://localhost:8080/IdentityService/Login";
                RESTSend consumer = new RESTSend(urlRequest,urlParameter);
                consumer.sendGet();
                JSONObject responseJSON = consumer.getJSON();
                out.print("<script>alert('" + responseJSON +"');</script>");
                
                /* Checking the response */
                String status = (String)responseJSON.get("status");
                String generatedToken = (String)responseJSON.get("token");
                if (status.equals("OK")){
                    response.sendRedirect("http://localhost:8080/WebApp/profile."
                            + "jsp?token="+generatedToken);
                }
                else{
                    response.sendRedirect("http://localhost:8080/WebApp/login."
                            + "jsp?message=error");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    %>

<head>
  <title>User Login</title>
  <link rel="stylesheet" type="text/css" href="css/style_login.css">
  <script src="script.js"></script>
</head>
<body>
<div class="container">
    <div class="form">
      <form class="login-form" method="GET">
        <div class=header><p>LOGIN</p></div>
        <div class="input">
          <p>Username</p>
          <input type="text" name="username"/>
        </div>
        <div class="input">
          <p>Password</p>
          <input type="password" name="password"/>
        </div>
        <a class="message" href="register.jsp">Belum daftar? Buat akun</a>
        <div class="button">
          
          <button type="submit" id="submit_btn" name="submit"><p>GO!</p></button>  
        </div>
      </form>
    </div> 
</div>
</body>
 
</html>