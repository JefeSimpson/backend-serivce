package org.github.jefesimpson.backend.service.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class User {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private LocalDate createDate;
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private UserRole userRole;

    public User() {
    }

    public User(int id, String login, String password, LocalDate createDate, UserRole userRole) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.createDate = createDate;
        this.userRole = userRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(createDate, user.createDate) &&
                userRole == user.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, createDate, userRole);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", createDate=" + createDate +
                ", userRole=" + userRole +
                '}';
    }
}
