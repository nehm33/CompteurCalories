package servlets;

import beans.User;
import db.DaoException;
import db.DaoFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Login extends HttpServlet {
       
    public Login() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = new User();
			String username = request.getParameter("username");
			user.setUsername(username);
			user.setPassword(request.getParameter("password"));
			User searchedUser = DaoFactory.getInstance().getUserDao().get(username);
			if (user.equals(searchedUser)) {
				response.sendRedirect("/CompteurCalories/Journal");
			} else {
				request.setAttribute("error", "Username or password incorrect");
				this.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
			}
		} catch (DaoException e) {
			request.setAttribute("error", e.getMessage());
			this.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

}
