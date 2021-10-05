FROM alpine

# install node
RUN apk add nodejs npm
# set working directory
WORKDIR /root/review-police
# copy project file
COPY ./package.json .
# install node packages
RUN npm set progress=false && \
    npm config set depth 0 && \
    npm install --only=production && \
    npm cache verify
# copy app files
COPY ./account-mappings.json .
COPY ./.env .
COPY ./src ./src
# application server port
EXPOSE 3000
# default run command
CMD [ "npm", "start" ]