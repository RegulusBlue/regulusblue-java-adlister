package com.codeup.adlister.controllers;

import com.codeup.adlister.dao.DaoFactory;
import com.codeup.adlister.models.Ad;
import com.codeup.adlister.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "controllers.CreateAdServlet", urlPatterns = "/ads/create")
public class CreateAdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/ads/create.jsp")
            .forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //sticky forms with errors

        User user = (User) request.getSession().getAttribute("user");

        boolean adFormErrors = request.getParameter("title").isEmpty() || request.getParameter("description").isEmpty();

        if (adFormErrors) {
            if (request.getParameter("title").isEmpty()) {
                request.setAttribute("titleMissing", "Title is missing");
            } else {
            request.setAttribute("titleEntered", request.getParameter("title"));}

            if (request.getParameter("description").isEmpty()) {
                request.setAttribute("descriptionMissing", "Description is missing");
            } else {
            request.setAttribute("descriptionEntered", request.getParameter("description")); }

            try {
                request.getRequestDispatcher("/WEB-INF/ads/create.jsp").forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }

        } else {

            //creates new ad for current user

            Ad ad = new Ad(
                    user.getId(),
                    request.getParameter("title"),
                    request.getParameter("description")
            );

            //inserts ad into the database

            DaoFactory.getAdsDao().insert(ad);


            //  consider changing redirect to /profile until adIndex is setup
            response.sendRedirect("/ads");
        }
    }
}
