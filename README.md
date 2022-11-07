
# Documentor

An API to allow upload images by adding tags to help to locate those images

## Details

The API has different sections to help users to classify their information:

### Documents

The unit of work, it represents something that needs to be stored that might have one or many images associated, we need to 
add a `name`, a brief `description` and a set fo `tags` in order to help to identify this document

### Uploads

Images associated to a document, those can be just one or a set of them

### Tenants

A tenant is a client which might have different users, a tenant can manage documents and all the users of a tenant have the
same access to those documents

### Users

A specific user that logins into the API to be able to manage documents and uploads

## Security

This API is secured used Auth0 provider, there are 2 main roles defined:

* User, this role is required to use the application, create / update / delete documents and uploads
* Admin, this role is required to configure the application, create / update users and tenants

In order to be able to consume the API it is required to generate a `Bearer` token and add it as part of the request headers when consuming the API

## Storage

The uploads are stored using a S3 object storage, which give us the advantage to configure the uploaded objects as read only and access them as a CDN

## Application Configuration

### Database

API uses MongoDB to store the document and uploads metadata, it uses the following environment variables to define the connection string:

* `MONGODB_CONNECTION_STRING`: DB connection string
* `documentor.database`: Application property which defines the DB name, by default this value is set as `Documentor`

### Storage

In order to configure S3 storage the following environment variables are required:

* `AWS_ACCESS_KEY_ID`: access key id
* `AWS_SECRET_ACCESS_KEY`: access secret
* `AWS_ENDPOINT`: endpoint
* `AWS_REGION`: configured region
* `documentor.storage.base-directory`: application property that defines the base directory where the uploads are stored. By default it has the following value: `public/documentor/documents`
* `documentor.storage.bucket`: applicaton property that defines the storage S3 bucket, by default is is set with: `object-cavitos`
* `documentor.storage.subdomain.base.url`: application property that defines the base URL where the uploaded files can be accessed. By default it is set with `https://cdn.cavitos.net`

### Security

In order to configure Auth0, the following environment variables are required:

* `AUTH0_AUDIENCE`: API audience defined by provider
* `AUTH0_JWT_ISSUER`: URL used by the provider to authorize requests
* `DOCUMENTOR_CORS_ORIGINS`: Set of URLs that are authorized to perform requests to the API

The security provider can be replaced by just changing the `SecurityConfiguration` to use the preferred provider, 
this application has built using Spring Security.
