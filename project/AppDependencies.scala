import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  private val bootstrapVersion = "8.4.0"
  private val hmrcMongoVersion = "1.7.0"
  private val playVersion      = "play-30"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-backend-$playVersion" % bootstrapVersion,
    "uk.gov.hmrc.mongo" %% s"hmrc-mongo-$playVersion"        % hmrcMongoVersion
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"       %% s"bootstrap-test-$playVersion"  % bootstrapVersion % Test,
    "uk.gov.hmrc.mongo" %% s"hmrc-mongo-test-$playVersion" % hmrcMongoVersion % Test
  )

}
