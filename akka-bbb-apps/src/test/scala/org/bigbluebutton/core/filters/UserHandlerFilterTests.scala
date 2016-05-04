package org.bigbluebutton.core.filters

import org.bigbluebutton.core.UnitSpec
import org.bigbluebutton.core.domain.{ Abilities2x, CanEjectUser, CanRaiseHand }

class UserHandlerFilterTests extends UnitSpec {
  it should "eject user" in {
    object DefPerm extends DefaultAbilitiesFilter
    val perm: Set[Abilities2x] = Set(CanEjectUser, CanRaiseHand)

    assert(DefPerm.can(CanEjectUser, perm))
  }

  it should "not eject user" in {
    object DefPerm extends DefaultAbilitiesFilter
    val perm: Set[Abilities2x] = Set(CanRaiseHand)

    assert(DefPerm.can(CanEjectUser, perm) != true)
  }
}