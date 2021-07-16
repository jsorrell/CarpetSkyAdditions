package com.jsorrell.skyblock.helpers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// Far from the best way of doing this.
// This is stored in memory only and doesn't persist through server restarts.
// In this case, it doesn't really matter unless someone restarts the server after a charged creeper
// kills the dragon but before the head is dropped.
public class DragonShouldDropHeadHelper {
  public static final Set<UUID> UUIDS = new HashSet<>();
}
