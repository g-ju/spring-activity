package main.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Users")
public class User
{
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String pwd;
    private String role;

    User()
    {

    }

    User(Long id, String username, String pwd, String role)
    {
        this.id = id;
        this.username = username;
        this.pwd = pwd;
        this.role = role;
    }

    User(String username, String pwd, String role)
    {
        this.username = username;
        this.pwd = pwd;
        this.role = role;
    }

    public UserDAO mapToDAO()
    {
        return new UserDAO(id, username, role);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username) &&
               Objects.equals(pwd, user.pwd) &&
               Objects.equals(role, user.role);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, username, pwd, role);
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + this.id +
               ", username='" + this.username + '\'' +
               ", password='" + this.pwd + '\'' +
               ", role = '" + this.role + "'}";
    }
}
