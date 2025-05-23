
# vat-registration-stub

A stub implementation for VAT registration Endpoints

### Locally
`sbt run` or `sbt 'run 8082'`

## Testing
`sbt clean coverage test it/test coverageReport`

### Formatting code
This library uses [Scalafmt](https://scalameta.org/scalafmt/), a code formatter for Scala. The formatting rules configured for this repository are defined within [.scalafmt.conf](.scalafmt.conf). Prior to checking in any changes to this repository, please make sure all files are formatted correctly.

To apply formatting to this repository using the configured rules in [.scalafmt.conf](.scalafmt.conf) execute:

```
sbt scalafmtAll
```

To check files have been formatted as expected execute:

```
sbt scalafmtCheckAll scalafmtSbtCheck
```
### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").