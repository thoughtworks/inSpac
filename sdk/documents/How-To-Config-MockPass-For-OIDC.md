# How to config MockPass for OIDC

- [Download MockPass](#Download-MockPass)
- [How to start MockPass](#How-to-start-MockPass)
- [API](#API)


## Download MockPass

Download [MockPass](https://github.com/ThoughtWorksInc/SEA-SC-OpenID/tree/mockpass)


## How to start MockPass

### Install
```
npm install
```

### Run
```
export SHOW_LOGIN_PAGE=true

npm run start
```


## API

Singpass:

- http://localhost:5156/singpass/authorize - OIDC login redirect with optional page
- http://localhost:5156/singpass/token - receives OIDC authorization code and returns id_token
