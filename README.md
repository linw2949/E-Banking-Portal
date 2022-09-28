<a name="readme-top"></a>
<!-- PROJECT LOGO -->
<br />
<div align="center">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  <h3 align="center">E-Banking-Portal</h3>

  <p align="center">
    An simple E-Banking-Portal example 
    <br />
    <a href="http://34.134.41.176:60000/swagger-ui/#/" target="_blank">View Demo</a>
    ·
    <a href="https://github.com/WinnieLinshi/E-Banking-Portal/issues" target="_blank">Report Bug</a>
    ·
    <a href="https://github.com/WinnieLinshi/E-Banking-Portal/issues" target="_blank">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#left_speech_bubbleabout-the-project">About The Project</a>
      <ul>
        <li><a href="#pickbuilt-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#geargetting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#nut_and_boltusage">Usage</a></li>
    <li><a href="#octocat-table-schema">Table Schema</a></li>
    <li><a href="#iphonecontact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## :left_speech_bubble:	About The Project

[![Product Name Screen Shot][product-screenshot]](http://34.134.41.176:60000/swagger-ui/#/)

This is a simple e-Banking Portal that implements a reusable REST API for returning the paginated list of money account transactions created in an arbitrary calendar month for a given customer who is logged in the portal. For each transaction ‘page’ return the total credit and debit values at the current exchange rate (from [the third-party provider](https://apilayer.com/marketplace/exchangerates_data-api)). The source of the list of transactions is consumed from a Kafka topic. There is a [Docker image out of the application](https://registry.hub.docker.com/layers/winnie2949/demo/1.4/images/sha256-57cd27716e1203aaada15796c429b3f89264aedc335dbced652a5d7e5fb9c29f?context=explore) and [the configuration](https://github.com/WinnieLinshi/E-Banking-Portal/blob/master/docker-k8s-demo.yaml) for deploying it to Kubernetes.

[Demo link](http://34.134.41.176:60000/swagger-ui/#/) here is the service that I deployed to [GKE](https://cloud.google.com/) according to the k8s configuration file provided above :slightly_smiling_face::+1:

<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>

### :pick:Built With 
[![Java][Java]][Java-url]
[![Kafka][Kafka]][Kafka-url]
[![Spring boot][Spring boot]][Spring boot-url]
[![K8s][K8s]][K8s-url]
[![CircleCI][CircleCI]][CircleCI-url]

<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>



<!-- GETTING STARTED -->
## :gear:Getting Started

There are many ways on setting up this project locally, but the easiest and most recommended way is to use a docker container!
To get a local copy up and running follow these simple steps. :trollface:

Of course, you can also choose not to use docker, run the code directly in intellij, and then modify the part you want to change
### Prerequisites

<details><summary>:electron:if you want to use a docker container</summary>

1. First, you must have [desktop docker.](https://www.docker.com/)
2. Second, download the [docker-compose.yaml](https://github.com/WinnieLinshi/E-Banking-Portal/blob/master/docker-compose.yaml) file of this project.
3. Once the download finished, execute the following commands in the same path of this file to pull docker image and run it.

  ```bash
  docker-compose -f docker-compose.yaml up -d
  ```

Until those 3 container zookeeper, kafka, demo are all Running, you can go to http://localhost:8080/swagger-ui/ see the project running as you can see above.
</details>
<details><summary>:atom:	if you want to run the code directly in intellij </summary>

1. First you have to have [intellij.](https://www.jetbrains.com/idea/download/#section=windows)
2. [Set up kafka zookeeper & kafka server](https://kafka.apache.org/quickstart) stop after step2.
3. pull [this project](https://github.com/WinnieLinshi/E-Banking-Portal) to your local repository.
4. run [DemoApplication.java](https://github.com/WinnieLinshi/E-Banking-Portal/blob/master/src/main/java/com/winnie/demo/DemoApplication.java) in IDE.

</details>

**or you can use [the demo](http://34.134.41.176:60000/swagger-ui/#/) :triangular_flag_on_post: directly**

<!-- USAGE EXAMPLES -->
## :nut_and_bolt:Usage

The page you see in http://localhost:8080/swagger-ui/ is an api list page [swagger](https://swagger.io/) that requires login to use, so please first use the api /authenticate under jwt-authentication-controller, and set the account to winnie and password to password, the response will give you a jwttoken.:lock:

Then copy and paste this jwttoken to the Authorize value area above, which is the hearer of the api.

After pasting, please add "Bearer " at the front then click authorize button, this is the norm.
By authorizing you can use other APIs!
<details>
<summary>send-kafka-controller POST:key:/SendKafka</summary>

This is a simple Kafka producer function.
Request format specification:

```json
{
    "amount": number not null
    "currency": 3 uppercase English letters, that follows [ISO 4217](https://zh.wikipedia.org/wiki/ISO_4217) : Specification for currency and funding code tables, e.g.,"TWD".
    "date": YYYYMMDD, e.g., "20220922"
    "description": no more than 20 letters, e.g.,"Online payment CHF".
    "iban":  no more than 26 letters, e.g.,"CH93-0000-0000-0000-0000-0".
    "id": no more than 40 letters, e.g.,"89d3o179-abcd-465b-o9ee-e2d5f6ofEld46".And id must not be repeated with the previously sent request input!
}
```
The request data will be consumed by kafka of the original service and written to the database.

Response: The content of the message successfully sent to kafka

If Http-code is not 200, it means there are errors in formats, permissions, repetitions, etc.

</details>

<details>
<summary>transaction-controller GET:key: /transaction/{iban}/page={pageNo}&pageSize={pageSize}</summary>
The api that allows the logged in person to view his own account transaction records in the past year.

So, Winnie, who is logged-on in the portal, can only check her own account transaction information.

Response: The query results are sorted from new to old, total credit and debit are converted using the exchange rate on the day of the transaction, and the amount converted is in euros.

If Http-code is not 200, it means there are errors in formats, permissions, etc.

</details>

<details>
<summary>transaction-controller POST:key: /transaction/create
</summary>
Directly adding transaction data here does not pass through the production and consumption of kafka
Request format specification is the same as SendKafka above.


Response: The content of the message successfully added to DB

If Http-code is not 200, it means there are errors in formats, permissions, repetitions, etc.
</details>

<!-- TABLE SCHEMA -->
## :octocat: Table Schema
**These are the initial data for this project**

### TRANSACTION
| ID:old_key:           | CURRENCY     | IBAN         | DATE     | DESCRIPTION  | AMOUNT |
|--------------|--------------|--------------|----------|--------------|-------|
| 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46 | CHF  | CH93-0000-0000-0000-0000-0 | 20220921 | Online payment CHF | 75    |

### ACCOUNT
 USER_ID:old_key: | IBAN  :old_key:            |
---------|----------------------------|
| winnie  | CH93-0000-0000-0000-0000-0 |
| winnie  | CH93-0000-0000-0000-0000-1 |
| winnie  | CH93-0000-0000-0000-0000-2 |
| lily    | CH93-0000-0000-0000-0000-3 |
| lily  | CH93-0000-0000-0000-0000-4 |

### USER
USER_ID:old_key: | PASSWORD |
---------|----------|
| winnie  | $2a$10$vVMom6S/OlDVjTQHTEoi3.5t00JLD/nsSqroEM1gMJ4wp40I4L3de | 
| lily  | $2a$10$cKvWBwka9DTYMMmbM3gR..psmiflcI9tQA37dwnM/kaipPv5ig.zS |

The 2 User's password clear code is "password"
<!-- CONTACT -->
## :iphone:	Contact

 [![LinkedIn][linkedin-shield]][linkedin-url]  
 **Winnie Lin** - [linw2949@gmail.com](mailto:linw2949@gmail.com)


<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/winnielin2949/
[product-screenshot]: images/screenshot.png
[Kafka]: https://media.geeksforgeeks.org/wp-content/uploads/20220214105957/SpringBootProducerConsumer.jpg
[Kafka-url]: https://kafka.apache.org/
[Spring boot]: https://www.split.io/wp-content/uploads/2021/05/BLOG-SpringBoot_Docker.png
[Spring boot-url]: https://spring.io/
[Java]: https://miro.medium.com/max/1400/1*vFiGOTV1S8yz0RTIQteTjw.png
[Java-url]: https://start.spring.io/
[K8s]: images/SpringBoot-K8s-diagram.png
[K8s-url]: https://cloud.google.com/
[CircleCI]: https://miro.medium.com/max/1400/1*Q2ru6CvvLq2phvQZ8Da0wA.png
[CircleCI-url]: https://circleci.com/
