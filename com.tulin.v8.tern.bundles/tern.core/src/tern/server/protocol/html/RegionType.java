package tern.server.protocol.html;

/**
 * Script region type :
 * 
 * <ul>
 * <li>{@link #RegionType#OPEN_START_SCRIPT} : open start script region type.
 * Example : &lt;script</li>
 * <li>{@link #RegionType#CLOSE_START_SCRIPT} : close start script region type.
 * Example : &gt;</li>
 * <li>{@link #RegionType#END_SCRIPT} : open start script region type. Example :
 * &lt;/script&gt;</li>
 * </ul>
 *
 */
public enum RegionType {

	OPEN_START_SCRIPT, CLOSE_START_SCRIPT, END_SCRIPT;
}
