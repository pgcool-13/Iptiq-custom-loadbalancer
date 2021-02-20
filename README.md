# Iptiq-custom-loadbalancer
Custom loadbalancer with strategies of random and roundrobin scheduling

Objective(Task description):
----------------------------
Step 1 – Generate provider
  Generate a Provider that, once invoked on his get() method, retrieve an unique identifier (string) of the provider instance
  
Step 2 – Register a list of providers
  Register a list of provider instances to the Load Balancer - the maximum number of providers accepted from the load balancer is 10
  
Step 3 – Random invocation
  Develop an algorithm that, when invoking multiple times the Load Balancer on its get() method, should cause the random invocation of the get() method of any    r   registered provider instance.
Step 4 – Round Robin invocation
  Develop an algorithm that, when invoking multiple times the Load Balancer on its get() method, should cause the round-robin (sequential) invocation of the get()     method of the registered providers.
Step 5 – Manual node exclusion / inclusion
  Develop the possibility to exclude / include a specific provider into the balancer
Step 6 – Heart beat checker
  The load balancer should invoke every X seconds each of its registered providers on a special method called check() to discover if they are alive – if not, it       should exclude the provider node from load balancing.
Step 7 – Improving Heart beat checker
  If a node has been previously excluded from the balancing it should be re-included if it has successfully been “heartbeat checked” for 2 consecutive times
Step 8 – Cluster Capacity Limit
  Assuming that each provider can handle a maximum number of Y parallel requests, the Balancer should not accept any further request when it has (Y*aliveproviders)   incoming requests running simultaneously
  
Introduction:
-------------
This is a assignment project for implementing a cuatom loadbalancer with features of registering providers, random and roundrobin scheduling, add/remove providers, providers health check and loadbalancer capacity limit

Tools and technologies used:
----------------------------
1. Java 8 (Language)
2. SpringBoot (Framework)
3. Docker
4. Docker-compose
5. Lombok (Remove boilerplate code)
6. Spring Rest template (for http call)

Steps to configure for running the project:
-------------------------------------------
1. docker-compose build
2. docker-compose up

Steps to test the project:
-------------------------------------------
To get unique provider id -> GET endpoint: http://localhost:8080/uniqueId

Steps to test other features:
-------------------------------------------
1. To exclude / include a specific provider into the balancer -> make changes in "array.of.provider" property as desired in application.properties file of loadbalancer
2. To change the scheduling strategy -> Change the property "invocation.method" to Random or RoundRobin in application.properties file of loadbalancer. Current set is RoundRobin

Assumptions/ Known facts
-------------------------------------------
1. Each provider can support Y=2 parallel requests. Can be changed through "instance.per.provider" property as desired in application.properties file of loadbalancer
2. In case any of the containers get shut with 137 exit code, please increase docker memory through Docker dashboard -> setting -> Resources -> Advanced tab
