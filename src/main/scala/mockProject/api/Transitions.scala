package mockProject.api

import scala.concurrent.duration.FiniteDuration

trait StartDefined

sealed trait PrefixDefinition {
  val _when: List[String]
}

sealed trait MockDefinition extends PrefixDefinition {
  val _what: String
}

sealed trait DelayedMockDefinition extends MockDefinition {
  val _delay: FiniteDuration
}

case class MockProps(when: List[String],
                     what: String,
                     delayOption: Option[FiniteDuration])

object Transition {

  implicit class TransitionAfterLet(startDefined: StartDefined) {
    def whenGetting(when: String): PrefixDefinition = new PrefixDefinition {
      override val _when: List[String] = List(when)
    }
  }

  implicit class TransitionAfterWhenDefined(whenDefined: PrefixDefinition) {
    def andWhenGetting(when: String): PrefixDefinition = new PrefixDefinition {
      override val _when: List[String] = whenDefined._when :+ when
    }

    def send(what: String): MockDefinition = new MockDefinition {
      override val _when: List[String] = whenDefined._when
      override val _what: String = what
    }
  }

  implicit class TransitionAfterWhenAndWhatDefined(
    whenAndWhatDefined: MockDefinition
  ) {
    def withDelay(finiteDuration: FiniteDuration): DelayedMockDefinition =
      new DelayedMockDefinition {
        override val _delay: FiniteDuration = finiteDuration
        override val _what: String = whenAndWhatDefined._what
        override val _when: List[String] = whenAndWhatDefined._when
      }
  }

  implicit def MockDefinitionToMockProps(
    mockDefinition: MockDefinition
  ): MockProps = {
    MockProps(mockDefinition._when, mockDefinition._what, None)
  }

  implicit def DelayedMockDefinitionToMockProps(
    delayedMockDefinition: DelayedMockDefinition
  ): MockProps = {
    MockProps(
      delayedMockDefinition._when,
      delayedMockDefinition._what,
      Some(delayedMockDefinition._delay)
    )
  }
}
