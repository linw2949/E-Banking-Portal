<a name="readme-top"></a>
<!-- PROJECT LOGO -->
<br />
<div align="center">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  <h3 align="center">E-Banking-Portal</h3>

  <p align="center">
    An simple E-Banking-Portal example 
    <br />
    <a href="http://34.72.139.232:60000/swagger-ui/#/">View Demo</a>
    ·
    <a href="https://github.com/WinnieLinshi/E-Banking-Portal/issues">Report Bug</a>
    ·
    <a href="https://github.com/WinnieLinshi/E-Banking-Portal/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](http://34.72.139.232:60000/swagger-ui/#/)

This is a simple e-Banking Portal that implements a reusable REST API for returning the paginated list of money account transactions created in an arbitrary calendar month for a given customer who is logged in the portal. For each transaction ‘page’ return the total credit and debit values at the current exchange rate (from the third-party provider). The source of the list of transactions is consumed from a Kafka topic. There is a [Docker image out of the application](https://registry.hub.docker.com/layers/winnie2949/demo/1.4/images/sha256-57cd27716e1203aaada15796c429b3f89264aedc335dbced652a5d7e5fb9c29f?context=explore) and the configuration for deploying it to Kubernetes.

<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>

### Built With 
[![Kafka][Kafka]][Kafka-url]
[![Spring boot][Spring boot]][Spring boot-url]

<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

[docker download](https://www.docker.com/) to open this project locally

Once the download finished, you can use command line pull docker image.

  ```sh
  docker-compose -f docker-compose.yaml up -d
  ```

Wait until those three container zookeeper, kafka, demo are all Running, go to http://localhost:8080/swagger-ui/#/


<!-- USAGE EXAMPLES -->
## Usage



<!-- CONTACT -->
## Contact

 [![LinkedIn][linkedin-shield]][linkedin-url] - Winnie Lin - linw2949@gmail.com


<p align="right"><a href="#readme-top"><img src="images/back.png" alt="back" width="40" height="40"></a></p>

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/winnielin2949/
[product-screenshot]: images/screenshot.png
[Kafka]: https://i0.wp.com/piotrminkowski.com/wp-content/uploads/2022/02/Screenshot-2022-02-04-at-13.28.25.png?resize=696%2C341&ssl=1
[Kafka-url]: https://kafka.apache.org/
[Spring boot]: https://www.split.io/wp-content/uploads/2021/05/BLOG-SpringBoot_Docker.png
[Spring boot-url]: https://spring.io/
