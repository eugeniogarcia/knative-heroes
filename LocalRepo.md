docker run -d -p 5000:5000 --restart=always --name registry registry:2

docker run -d -p 5000:5000 --restart=always --name registry registry:latest

http://localhost:5000/v2/_catalog