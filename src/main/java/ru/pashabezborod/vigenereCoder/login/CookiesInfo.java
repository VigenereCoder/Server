package ru.pashabezborod.vigenereCoder.login;

import org.springframework.stereotype.Component;
import ru.pashabezborod.vigenereCoder.models.User;
import ru.pashabezborod.vigenereCoder.util.UserDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CookiesInfo {

    private final List<UserInfo> usersList = new ArrayList<>();

    public long addCookie(String userName, String user_agent) {
        long cookie = new Random(System.currentTimeMillis()).nextLong();
        usersList.add(new UserInfo(userName, user_agent, cookie, System.currentTimeMillis()));
        System.out.println(userName + " " + user_agent + " " + cookie);
        return cookie;
    }

    public void deleteCookie(long cookie, String user_agent) {
        System.out.println("DELETING " + cookie + " " + user_agent);
        usersList.removeIf(userInfo -> userInfo.cookie == cookie && userInfo.user_agent.equals(user_agent));
    }

    public void deleteCookie(String name) {
        usersList.removeIf(userInfo -> userInfo.userName.equals(name));
        System.out.println("cookie deleted " + name);
    }

    public boolean checkCookie(long cookie, String user_agent) {
        return usersList.stream().anyMatch(userInfo -> userInfo.cookie == cookie && userInfo.user_agent.equals(user_agent));
    }

    public String getUserNameByCookie(long cookie) {
        return usersList.stream()
                .filter(userInfo -> userInfo.cookie == cookie)
                .findAny()
                .orElseThrow(() -> new UserDataException("Invalid user-agent. You should log in first"))
                .userName;
    }

    private record UserInfo(
            String userName,
            String user_agent,
            long cookie,
            long timestamp) {}
}
