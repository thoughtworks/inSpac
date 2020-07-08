# KeyCloak Basic Introduction

- [What is KeyCloak](#what-is-keycloak)
- [Core Concepts and Terms](#core-concepts-and-terms)
- [Workflow](#workflow)


## What is KeyCloak

Keycloak is an open source software product to allow single sign-on with Identity Management and Access Management aimed at modern applications and services.


## Core Concepts and Terms

There are some key concepts and terms you should be aware of before attempting to use Keycloak to secure your web applications and REST services. For more information check out [Core Concepts and Terms
](https://www.keycloak.org/docs/latest/server_admin/index.html#core-concepts-and-terms)

**realms**\
A realm manages a set of users, credentials, roles, and groups. A user belongs to and logs into a realm. Realms are isolated from one another and can only manage and authenticate the users that they control.

**clients**\
Clients are entities that can request Keycloak to authenticate a user. Most often, clients are applications and services that want to use Keycloak to secure themselves and provide a single sign-on solution. Clients can also be entities that just want to request identity information or an access token so that they can securely invoke other services on the network that are secured by Keycloak.

**client scopes**\
When a client is registered, you must define protocol mappers and role scope mappings for that client. It is often useful to store a client scope, to make creating new clients easier by sharing some common settings. This is also useful for requesting some claims or roles to be conditionally based on the value of scope parameter. Keycloak provides the concept of a client scope for this.

**roles**\
Roles identify a type or category of user. Admin, user, manager, and employee are all typical roles that may exist in an organization. Applications often assign access and permissions to specific roles rather than individual users as dealing with users can be too fine grained and hard to manage.

**identity provider**\
An identity provider (IDP) is a service that can authenticate a user. Keycloak is an IDP.

**user federation provider**\
Keycloak can store and manage users. Often, companies already have LDAP or Active Directory services that store user and credential information. You can point Keycloak to validate credentials from those external stores and pull in identity information.

**authentication**\
The process of identifying and validating a user.

**groups**\
Groups manage groups of users. Attributes can be defined for a group. You can map roles to a group as well. Users that become members of a group inherit the attributes and role mappings that group defines.

**users**\
Users are entities that are able to log into your system. They can have attributes associated with themselves like email, username, address, phone number, and birth day. They can be assigned group membership and have specific roles assigned to them.

**session**\
When a user logs in, a session is created to manage the login session. A session contains information like when the user logged in and what applications have participated within single-sign on during that session. Both admins and users can view session information.

**events**\
Events are audit streams that admins can view and hook into.


## Workflow

Overview\
![Keycloak Overview](/images/keycloak_overview.png)

![Login Flow](/images/login_flow.png)

For logout you need to add `Logout URL` in `OpenID Connect Config`\
![Logout Config](/images/logout_config.png)
