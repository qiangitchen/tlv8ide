package tern.server.protocol.html;

/**
 * {@link IState} implementation with a single {@link ScriptTagRegion}.
 *
 */
class State implements IState {

	private final ScriptTagRegion tagRegion;
	private int index = 0;
	private Region nextRegionToFind;

	public State(ScriptTagRegion tagRegion) {
		this.tagRegion = tagRegion;
		this.nextRegionToFind = tagRegion.getOpenStartScript();
	}

	@Override
	public Region update(char c) {
		// is the given character and current index matches the next region to
		// find?
		MatchResult result = nextRegionToFind.match(c, index);
		switch (result) {
		case MATCHING:
			// the given character matches the region.
			// increment the index to check if the next character matches the
			// region (on the next call of update).
			index++;
			break;
		case NO_MATCHING:
			// no matching, retry the match of the region.
			index = 0;
			break;
		case MATCHED:
			// the given character matches the full region.
			index = 0;
			switch (nextRegionToFind.getType()) {
			case OPEN_START_SCRIPT:
				// <script element found
				// next region to find is >
				nextRegionToFind = tagRegion.getCloseStartScript();
				// return the current region which is <script
				return tagRegion.getOpenStartScript();
			case CLOSE_START_SCRIPT:
				// > found
				// next region to find is </script>
				nextRegionToFind = tagRegion.getEndScript();
				// return the current region which is >
				return tagRegion.getCloseStartScript();
			case END_SCRIPT:
				// </script> element found
				// next region to find is <script
				nextRegionToFind = tagRegion.getOpenStartScript();
				// return the current region which is </script>
				return tagRegion.getEndScript();
			}
			break;
		}
		return null;
	}

	@Override
	public boolean isNextRegionToFindType(RegionType type) {
		return this.nextRegionToFind.getType() == type;
	}

	@Override
	public void reset() {
		index = 0;
	}

}