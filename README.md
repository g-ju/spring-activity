
# Spring Activity API

REST API service for CRUD operations of activities and users. Users are able to make lists of activities.

Uses embedded H2 database and basic authentication.




## Run Locally

Clone the project

```bash
  git clone https://github.com/g-ju/spring-activity
```

Go to the project directory and run application

```bash
  mvn spring-boot:run
```

The application is initially loaded with two users and two activities. The users are:
- An admin, with both username and password being "admin"
- A user, with username "user" and password "password"


## API Reference

By default, the service will run on port 8080 (http://localhost:8080). The admin role ("ADMIN") is required on a user for some requests.

| Methods | Urls                                    | Description                                      | Permissions Required |
|---------|-----------------------------------------|--------------------------------------------------|----------------------|
| GET     | /activities                             | Return list of all activities                    | None                 |
| GET     | /activities/{id}                        | Get activity by {id}                             | None                 |
| POST    | /activities                             | Create new activity                              | Any user             |
| PUT     | /activities/{id}                        | Update activity with {id}                        | Any user             |
| DELETE  | /activities/{id}                        | Delete activity {id}                             | Admin only           |
| GET     | /users                                  | Return list of all users                         | Any user             |
| GET     | /users/{id}                             | Get user by {id}                                 | Any user             |
| POST    | /users                                  | Create new user                                  | None                 |
| DELETE  | /users/{id}                             | Delete user {id}                                 | Admin only           |
| PUT     | /users/{userId}/activities/{activityId} | Add activity {activityId} to user {userId}'s list| User with {userId}   |


## Usage/Examples

**Example 1:** POST /users without authentication, to create new user
```
{
    "username": "test_user",
    "pwd": "test_password",
    "role": "USER"
}
```
Response: 201 (Created)
```
{
    "id": 1,
    "username": "test_user",
    "role": "USER",
    "activities": []
}
```

**Example 2:** POST /activities using credentials above
```
{
   "name": "Socialise",
   "description": "Hang out with friends or meet new people"
}
```
Response: 201 (Created)
```
{
    "id": "1",
    "name": "Socialise",
    "description": "Hang out with friends or meet new people",
    "_links": {
        "self": {
            "href": "http://localhost:8080/activities/1"
        },
        "activities": {
            "href": "http://localhost:8080/activities"
        }
    }
}
```

**Example 3:** PUT /users/1/activities/1 to add new activity to user

Response: 200 (OK)
```
{
    "id": 1,
    "username": "test_user",
    "role": "USER",
    "activities": [
        {
            "id": "1",
            "name": "Socialise",
            "description": "Hang out with friends or meet new people"
        }
    ]
}
```


## Related

See the other branches for various stages of this application.
- /crud - Only CRUD operations for activities.
- /basic_auth - Enabling basic authentication, but only with an in memory user.
- /users - Adding in user entities, and using the users saved in the database for authentication.
- /activity_lists - Same as master.

