FROM node:18-alpine

WORKDIR /app

COPY index.js .
COPY start.sh .

RUN chmod +x start.sh


RUN npm init -y && npm install axios

CMD ["./start.sh"]
