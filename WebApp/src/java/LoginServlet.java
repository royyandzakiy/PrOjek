/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Zack
 */
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session;
        try(PrintWriter out = response.getWriter()) {
             
            
            // kirim data login ke Identity Service
            // validasi token yang didapat ke IdentityService
            // gunakan username utk masuk ke profile

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // kirim data ke Identity Service, terima JSON berupa token & expiry
            String URL_Target = "localhost:8080/IdentityService/Login";
            String URL_Param = "username=" + username + "password=" + password;
            
            RESTSend rest = new RESTSend(URL_Target, URL_Param);
            processRequest(request, response);
            
            try {
                rest.sendPost(); // kirim data, dapat json
                // --atau langsung kirim ke Servlet
            } catch (Exception ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // ekstrak info dari JSON. ambil token
            JSONObject output = rest.getJSON();
            String success = output.get("success").toString();
            
            if (success == "true") {
                String token = output.get("token").toString();
                String expiry = output.get("expiry").toString();
                String name = output.get("name").toString();

                // kirim data ke Identity Service, terima JSON berupa username
                URL_Target = "localhost:8080/IdentityService/TokenValidator";
                URL_Param = "token=" + token;
                rest = new RESTSend(URL_Target, URL_Param);
                try {
                    rest.sendPost(); // kirim data, dapat json
                } catch (Exception ex) {
                    Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                // ekstrak info dari JSON. ambil keterangan valid
                output = rest.getJSON();

                String tokenValid = output.get("tokenValid").toString();
                
                if (tokenValid == "true") {
                // simpan token di session
                    session = request.getSession(false);
                    session.setAttribute("username", username);
                    session.setAttribute("token", token);
                    session.setAttribute("expiry", expiry);
                    session.setAttribute("name", name);
                }
                
                // masuk ke profile
                response.sendRedirect("index.jsp");
            }
        } catch (JSONException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
