# COMPANIES LIST

An application providing tools for managing company's entity.

## Set Up Instructions

Firstly, for successfully running application, ensure you have installed on your PC JDK 17, Maven,
Docker and Postman.

Clone the repository:

`git clone git@github.com:oksana-rudenko/companies-list.git`

Build the project:

`mvn clean package`

Run the project. Try end-points by using Postman (or Swagger).
Use Json template for creating or updating entity:

{

    "name": "Morgan Stanley",
    "industry": "Capital Markets",
    "residence": "1585 Broadway New York, NY 10036 United States",
    "employeeAmount": 80000,
    "capitalization": 167335000
}

## Working with Postman

A few examples of trying project with Postman.
1) Creating new company
![img.png](img.png)
2) Getting company by id
![img_1.png](img_1.png)
3) Getting companies by industry
![img_2.png](img_2.png)
4) Updating company by id
![img_3.png](img_3.png)
5) Deleting company by id
![img_4.png](img_4.png)
