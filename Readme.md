# Bukkit RegionWarp
This is a Bukkit plugin that allows your users to warp to WorldGuard regions.

## Setup
1. Copy the jar in your `plugins/` directory.
2. Give your users the right permissions.
3. That's it!

## Permissions
By default only ops have all permissions.

* `regionwarp.all`: A user can warp to all regions.
* `regionwarp.owner`: A user can warp to its own regions (where he is the owner).
* `regionwarp.member`: A user can warp to regions where he is a member.

## Usage
First of all, you have to set the warp points of the regions. This works with WorldGuard's flags, so `/region flag <name> teleport me` sets the teleport for the region to your current location.

Then your users can type `/reg <name>` to get teleported to the region's warp point.

## Todo

* Add a shorter warp point command, that verifies that the warp point is in the region.