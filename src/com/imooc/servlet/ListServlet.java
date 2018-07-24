package com.imooc.servlet;

import com.imooc.bean.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表页面初始化控制
 * @author GaoHangHang
 * @date 2018/07/07 19:54
 **/
@SuppressWarnings("serial")
public class ListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            //获取指令和描述
            String command = req.getParameter("command");
            String description = req.getParameter("description");
            req.setAttribute("command",command);
            req.setAttribute("description",description);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/micro_message", "root", "root");
            StringBuilder sql =new StringBuilder("select ID,COMMAND,DESCRIPTION,CONTENT from MESSAGE where 1=1");
            List<String> paramList = new ArrayList<>();
            if (command!=null && !"".equals(command.trim())){
                sql.append(" and COMMAND=?");
                paramList.add(command);
            }
            if (description != null && !"".equals(description.trim())){
                sql.append(" and DESCRIPTION like '%' ? '%'");
                paramList.add(description);
            }
            PreparedStatement statement = conn.prepareStatement(sql.toString());
            //遍历参数替换
            for (int i = 0; i < paramList.size(); i++) {
                statement.setString(i+1,paramList.get(i));
            }
            //返回结果集
            ResultSet rs = statement.executeQuery();
            List<Message> messageList = new ArrayList<Message>();
            while (rs.next()) {
                Message message = new Message();
                messageList.add(message);
                message.setId(rs.getString("ID"));
                message.setCommand(rs.getString("COMMAND"));
                message.setDescription(rs.getString("DESCRIPTION"));
                message.setContent(rs.getString("CONTENT"));
            }
            req.setAttribute("messageList",messageList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("/WEB-INF/jsp/back/list.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }
}
