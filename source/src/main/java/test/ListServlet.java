package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dto.LoginUser;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/ListServlet")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// もしもログインしていなかったらログインサーブレットにリダイレクトする
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/webapp/LoginServlet");
			return;
		}

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

		// 検索ページにフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// もしもログインしていなかったらログインサーブレットにリダイレクトする
		HttpSession session = request.getSession();
		if (session.getAttribute("id") == null) {
			response.sendRedirect("/webapp/LoginServlet");
			return;
		}

		// ユーザ情報取得用
		LoginUser loginUser = (LoginUser) session.getAttribute("id");
		String userId = loginUser.getId();
		request.setAttribute("userId", userId);

		// リクエストパラメータを取得する
		request.setCharacterEncoding("UTF-8");

		String hiduke = request.getParameter("hiduke");
		String category = request.getParameter("category");
		String tag = request.getParameter("tag");
		String situation = request.getParameter("situation");
		String emotion = request.getParameter("emotion");
		String amount = request.getParameter("amount");

		Income condition = new Income();

		condition.setHiduke(hiduke);
		condition.setCategory(category);
		condition.setTag(tag);
		condition.setSituation(situation);
		condition.setEmotion(emotion);
		condition.setAmount(amount);

		condition.setUserId(userId);

		// 検索処理を行う
		
		IncomeDao incomeDao = new IncomeDao();
		ExpensesDao expensesDao = new ExpensesDao();
		PatienceDao patienceDao = new PatienceDao();

		List<Income> incomeList = incomeDao.select(userId);
		List<Expenses> expensesList = expensesDao.select(userId);
		List<Patience> patienceList = patienceDao.select(userId);

		request.setAttribute("incomeList", incomeList);
		request.setAttribute("expensesList", expensesList);
		request.setAttribute("patienceList", patienceList);
		
		
		// 検索結果をリクエストスコープに格納する
		request.setAttribute("incomeList", incomeList);

		// 結果ページにフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/list.jsp");
		dispatcher.forward(request, response);
	}
}
