This project is a secure RESTful API built with Spring Boot, providing full CRUD (Create, Read, Update, Delete)
operations for managing resources such as users, products, or custom entities. It implements JWT (JSON Web Token) 
authentication to ensure secure access to protected endpoints, enabling role-based authorization and stateless
session management.

-------------------------------------------------------------
We know that there are certain layer in the spring boot architecture let's start from the DispatcherServlet and Controller
basically when client send the request to the server and the server has this Dispatcher servlet as the fronty controlelr 
and the responsibility of the Dispatcher servlet is to find the correct method and endpoint and delegate it to the controller to their respective methods.
once we talk about spring security between this two spring security introduced new layer named Security Filter chain.
we call this filter chain because their are multiple filters present within the security layer. each request goes through the first 
filter and then the next filter and then the next and so on till the last filter and then finally it reach to the controller.
this particular design pattern is known as chain of responsibility. now we will be creating a custom filter chain and that v 
will be responsible for the authentication and authorization of the users using JWT that filter we call it as JWTAuthFilter orJWTAuthniticatioFilter.
the Default security filter chain we get is UsernamePasswordAuthenticationFilter. this is the default implementation by springboot
means once we add this security dependency it will redirect it to the login form if you try to access any endpoint this is 
because of this filter chain. now we will be adding our custom auth filter before this filter and we want our filter to present
in the begining and the job of our custom filter is to extract the token and perform the necessaory operations like login 
and all and deleget it to the next filter chain.
We create one JWTUtil class and the role of this util class is to generate the token and validate the token, and extract information form token,
information like what is the user name and what is the expriry and there are some claims as well.
and somewhere we need to configure the filter chain so that definition comes from one of the inbuilt class called HttpSecurity 
and the function this class provide HttpSecurity.built which result a filter chain. there are some class which we need to use here
are UserDetails, UserDetailsService,PasswordEncoder, AuthenticationManager,AuthenticationProvider.these are the classes required by the framework.
As we know we are sending the request using some header with Authorization or JWT token, but 
at one point of time we need to access the public endpoint for example login functionality, 
and we need to provide username and password here we are not passing any authentication header here
then that means this is the public endpoint where we are not applying our authentication filter.]
so we need some guy who can authenticate the username and password like we will check whether the 
username and password is exist in my database so for that purpose we need two implementation first one is
AuthenticationManager and this AuthenticationManager internally is going to use AuthenticationProvider.
so we can say that Authentication Manager handles the public endpoint here it is login endpoitn and 
authorize the user and provide the authentication token.

--------About JWT Token-------------
JSON Web Token(JWT) in this token we are measurely interested on the payload or Body, this payload
or body contains the key value pair, individual key value pair is called claim and this complete
JSON object is called as Claims and that sub, name, iat is not just a random keywords they are 
very well identified and recognised claim so sub means Subject, name means name, iat means issued at.
so this sub claim is going to be the username oe email id or the primary way to identified the user and this 
name claim is going to be the name of the user.
we will be interested out of this token will be two things one is subject and one is expiry and that we will be implemenmting 
in our code. we know user has password and role as well but we will not add the password and role in the token because
if we provide the password and if somebody able to decrypt it then it will not good things and if we provide the role then someone can 
change the role.
now how we will handle the password and role for us so what we do is we have subject handy with us
so we use this username i.e., subject and query our database to get the password and role then we will 
decide what access do we have. so to work with JWT tken we need to add one dependency 
<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.12.6</version>
</dependency>

Note:- we have three part of the token first part is Header which consist the alogorithm we used 
and the type and second part is Payload or Body which consist of SUbject(username), name and issuedAt date
and third part is Signature(signing details).
So to convert this JSON into the encrypted format we need a signing key its like a secret that known by the client and server onlu
and using that we can encode and decode the token.

When we extends OncePerRequestFilter in our custom authentication filter then it will have access to FilterChain.

To handle the public endpoint we need to use AuthenticationManager and it will use AuthenticationProvider.
so we need to create the bean for both of them.

We have to create the bean inside COnfiguration file 




