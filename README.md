# java-filmorate
Template repository for Filmorate project.
![This is db diagram](/Users/vsharapov/Documents/education/java_dev/java-filmorate/src/main/resources/QuickDBD-export.png)

Below are queries to retrieve some basic information from data base.

GET User All:
```
SELECT *
FROM users
```

GET Films All:
```
SELECT *
FROM films
```

GET User friends:
```
SELECT *
FROM friends
WHERE user_id=1;
```
