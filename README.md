# Inversion of Control Container
A `simple` annotation based ioc container that supports dependency injection.

### Supported Annotations
- `@Bean` - Signify a Method that produces a Bean on execution`
- `@Named` - Specify the name of a Bean
- `@Primary` - Specify the default bean to be selected in the event of conflicts
- `@Component` - A Bean that should be picked up while scanning (on startup) and have its dependencies injected
