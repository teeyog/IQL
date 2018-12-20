package iql.common.utils;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by UFO on 7/8/2016.
 */
public class MailUtils {

    /**
     * args[0]:接收人邮箱，以‘,’连接
     * args[1]:邮件标题
     * args[2]:邮件内容
     * @param args
     * @throws Exception
     */
    public static void sendMail(Properties props,String[] args) throws Exception{

        if(args.length != 3){
            System.out.println("请输入三个参数：\nargs[0]：接收人邮箱，以‘,’连接；\nargs[1]：邮件标题；\nargs[2]：邮件内容。");
            return;
        }
        String reciveEmails = args[0];
        String title = args[1];
        String content = args[2];

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);
        // 设置收件人  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
        InternetAddress[] internetAddressTo = new InternetAddress().parse(reciveEmails);
        message.setRecipients(RecipientType.TO, internetAddressTo);

        // 设置邮件标题
        message.setSubject(title);
        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");
        // 发送邮件
        Transport.send(message);
    }

    public static void main(String[] args) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}