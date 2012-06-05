package physics_game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelCache {
	private static final Map<String, LevelLayout> loaded = new HashMap<String, LevelLayout>();

	public static LevelLayout getLevel(String key) {
		return loaded.get(key);
	}

	public static void setLevel(String key, LevelLayout value) {
		loaded.put(key, value);
	}

	private static LevelLayout constructIntro1() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "intro2", "intro1", "intro1", 3);
	}

	private static LevelLayout constructIntro2() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "intro3", "intro2", "intro2", 3);
	}

	private static LevelLayout constructIntro3() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "tutorial", "intro3", "intro3", 5);
	}

	private static LevelLayout constructTutorialLevel() {
		int width = 1920;
		int height = 1080;
		RetractablePlatform placeholder;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		// ground platform
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		// left wall
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		// right wall
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		// top wall
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		footholds.put(Byte.valueOf((byte) 4), new Platform(300, 400, 200, 0));

		// footholds[5] = new Platform(725, 775, 1100, 175);
		footholds.put(Byte.valueOf((byte) 5), new Platform(975, 1075, 300, 0));

		// main center
		footholds.put(Byte.valueOf((byte) 7), new Platform(1000, 1300, 900, 700));
		// small to the right
		footholds.put(Byte.valueOf((byte) 8), new Platform(1300, 1500, 870, 850));
		// left
		footholds.put(Byte.valueOf((byte) 9), new Platform(950, 1000, 1100, 700));

		// top
		footholds.put(Byte.valueOf((byte) 10), new Platform(1000, 1500, 1100, 1050));
		// top to the right
		footholds.put(Byte.valueOf((byte) 11), new Platform(1500, 1951, 1100, 1060));

		// top part extending downwards
		footholds.put(Byte.valueOf((byte) 12), new Platform(1100, 1300, 1050, 940));

		// right wall, for exit
		footholds.put(Byte.valueOf((byte) 13), new Platform(1650, 1951, 800, 0));

		// wall blocking exit

		placeholder = new RetractablePlatform(Color.BLUE, 1650, 1700, 1060, 800);
		footholds.put(Byte.valueOf((byte) 14), placeholder);

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		boxes.add(new BoxSpawnInfo(new Position(401, 0), .5f, .5f, .5f));
		boxes.add(new BoxSpawnInfo(new Position(1100, 0), .5f, .5f, 1.0f));
		boxes.add(new BoxSpawnInfo(new Position(1225, 0), .5f, .5f, 1.0f));
		boxes.add(new BoxSpawnInfo(new Position(1350, 0), .5f, .5f, 1.0f));
		boxes.add(new BoxSpawnInfo(new Position(1001, 901), .4f, .1f, .4f));
		switches.add(new SwitchSpawnInfo(new Position(1450, 870), placeholder));
		tips.add(new OverlayInfo(new Position(0, 200), 400, 200, "jetpackOverlay"));
		tips.add(new OverlayInfo(new Position(500, 200), 400, 200, "beamOverlay"));
		tips.add(new OverlayInfo(new Position(1400, 400), 400, 200, "growOverlay"));
		tips.add(new OverlayInfo(new Position(1400, 1000), 400, 200, "shrinkOverlay"));
		tips.add(new OverlayInfo(new Position(1900, 1000), 400, 200, "switchOverlay"));

		doors.add(placeholder);

		return new LevelLayout(width, height, footholds, new Position(100, 100), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "mid1", "scrollingWindowBg", "mainBg", Double.POSITIVE_INFINITY);
	}

	private static LevelLayout constructMid1() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "level1", "mid1", "mid1", 3);
	}

	private static LevelLayout constructLevel2() {
		int width = 1920;
		int height = 2000;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		// ground platform
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		// left wall
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		// right wall
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		// top wall
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		// long piece
		footholds.put(Byte.valueOf((byte) 4), new Platform(0, 800, 700, 600));
		// small rectangle above
		footholds.put(Byte.valueOf((byte) 5), new Platform(0, 200, 740, 700));
		// small piece to left above small rectangle
		footholds.put(Byte.valueOf((byte) 6), new Platform(0, 25, 920, 740));

		// small piece to right above small rectangle
		footholds.put(Byte.valueOf((byte) 7), new Platform(175, 200, 920, 780));
		// long piece upwards
		footholds.put(Byte.valueOf((byte) 8), new Platform(0, 200, 1600, 920));
		// platform extends out
		footholds.put(Byte.valueOf((byte) 9), new Platform(200, 250, 1450, 1400));

		// left guarding door
		footholds.put(Byte.valueOf((byte) 10), new Platform(800, 850, 1700, 1550));

		RetractablePlatform platform = new RetractablePlatform(Color.BLUE, 800, 850, 1550, 1400);
		footholds.put(Byte.valueOf((byte) 11), platform);
		// bottom guarding door
		footholds.put(Byte.valueOf((byte) 12), new Platform(800, 1100, 1400, 1350));
		// right guarding door
		footholds.put(Byte.valueOf((byte) 13), new Platform(1050, 1100, 1700, 1400));
		// top guarding door
		footholds.put(Byte.valueOf((byte) 14), new Platform(800, 1100, 1700, 1650));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		boxes.add(new BoxSpawnInfo(new Position(650, 0), .5f, .5f, .75f));
		boxes.add(new BoxSpawnInfo(new Position(800, 0), .5f, .5f, .75f));
		boxes.add(new BoxSpawnInfo(new Position(725, 100), .5f, .5f, .75f));
		boxes.add(new BoxSpawnInfo(new Position(26, 740), .5f, .1f, .75f));

		switches.add(new SwitchSpawnInfo(new Position(201, 1450), platform));

		doors.add(platform);

		return new LevelLayout(width, height, footholds, new Position(100, 100), new Position(851, 1401), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "level3", "scrollingWindowBg", "mainBg", Double.POSITIVE_INFINITY);
	}

	private static LevelLayout constructLevel1() {
		int width = 1500;
		int height = 2000;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		// ground platform
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		// left wall
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		// right wall
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		// top wall
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		// center platform
		footholds.put(Byte.valueOf((byte) 4), new Platform(400, 600, 700, 600));

		// center-right platform near exit
		footholds.put(Byte.valueOf((byte) 5), new Platform(1050, 1250, 700, 600));
		// Wall blocking exit
		footholds.put(Byte.valueOf((byte) 6), new Platform(1050, 1150, 600, 0));

		// wall above and to the right of exit
		footholds.put(Byte.valueOf((byte) 7), new Platform(1350, 1500, 700, 600));

		// Platform to the right near door
		footholds.put(Byte.valueOf((byte) 8), new Platform(1450, 1500, 600, 0));

		RetractablePlatform plat1 = new RetractablePlatform(Color.YELLOW, 1250, 1350, 700, 670);
		footholds.put(Byte.valueOf((byte) 9), plat1);
		RetractablePlatform plat2 = new RetractablePlatform(new Color(128, 0, 0), 1250, 1350, 665, 635);
		footholds.put(Byte.valueOf((byte) 10), plat2);
		RetractablePlatform plat3 = new RetractablePlatform(new Color(240, 255, 255), 1250, 1350, 630, 600);
		footholds.put(Byte.valueOf((byte) 11), plat3);
		RetractablePlatform plat4 = new RetractablePlatform(new Color(210, 105, 30), 1125, 1175, 1000, 700);
		footholds.put(Byte.valueOf((byte) 12), plat4);
		// top floor
		footholds.put(Byte.valueOf((byte) 13), new Platform(0, 1200, 1075, 1025));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		switches.add(new SwitchSpawnInfo(new Position(550, 700), plat4));
		switches.add(new SwitchSpawnInfo(new Position(1050, 700), plat2));
		switches.add(new SwitchSpawnInfo(new Position(500, 1075), plat1));
		switches.add(new SwitchSpawnInfo(new Position(200, 1075), plat3));

		boxes.add(new BoxSpawnInfo(new Position(200, 0), .75f, .25f, 1.0f));
		boxes.add(new BoxSpawnInfo(new Position(1425, 700), .2f, .2f, .75f));
		rects.add(new RectangleSpawnInfo(new Position(400, 0), .5f, .5f, 1.5f));

		tips.add(new OverlayInfo(new Position(100, 1200), 400, 200, "rotateOverlay"));

		doors.add(plat1);
		doors.add(plat2);
		doors.add(plat3);
		doors.add(plat4);

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1200, 0), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "level2", "scrollingWindowBg", "mainBg", Double.POSITIVE_INFINITY);
	}

	private static LevelLayout constructLevel3() {
		int width = 2500;
		int height = 2000;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		// ground platform
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		// left wall
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		// right wall
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		// top wall
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		// left part for switches
		footholds.put(Byte.valueOf((byte) 4), new Platform(0, 150, 200, 0));

		// right wall
		footholds.put(Byte.valueOf((byte) 5), new Platform(700, 1100, 225, 0));

		// left floating wall
		footholds.put(Byte.valueOf((byte) 6), new Platform(300, 350, 750, 500));
		// bottom of floating wall
		footholds.put(Byte.valueOf((byte) 7), new Platform(300, 550, 500, 450));
		// right of the float wall is another
		footholds.put(Byte.valueOf((byte) 8), new Platform(550, 600, 500, 150));

		// left upper part
		footholds.put(Byte.valueOf((byte) 9), new Platform(500, 600, 900, 625));

		// part off of left upper

		footholds.put(Byte.valueOf((byte) 10), new Platform(500, 1000, 1000, 900));

		// part blocking access to left

		footholds.put(Byte.valueOf((byte) 11), new Platform(600, 650, 675, 567));

		// step 1
		footholds.put(Byte.valueOf((byte) 13), new Platform(775, 1100, 275, 225));

		// step 2
		footholds.put(Byte.valueOf((byte) 14), new Platform(850, 1100, 325, 275));
		// step 3
		footholds.put(Byte.valueOf((byte) 15), new Platform(925, 1100, 375, 325));

		// large right floor
		footholds.put(Byte.valueOf((byte) 16), new Platform(1100, 2500, 660, 0));
		// part coming down from top
		footholds.put(Byte.valueOf((byte) 17), new Platform(850, 1000, 900, 550));

		// 2 float platsform holding n
		footholds.put(Byte.valueOf((byte) 18), new Platform(1075, 1225, 1200, 1150));
		footholds.put(Byte.valueOf((byte) 19), new Platform(1325, 1475, 1200, 1150));

		// float upper right, holding retractables

		footholds.put(Byte.valueOf((byte) 20), new Platform(1900, 2000, 1000, 825));

		// part guarding door
		footholds.put(Byte.valueOf((byte) 21), new Platform(2150, 2250, 1125, 825));

		footholds.put(Byte.valueOf((byte) 22), new Platform(2100, 2150, 860, 825));
		footholds.put(Byte.valueOf((byte) 23), new Platform(2250, 2500, 1125, 1075));

		// step 4
		footholds.put(Byte.valueOf((byte) 24), new Platform(1000, 1100, 425, 375));

		footholds.put(Byte.valueOf((byte) 25), new Platform(1000, 1050, 875, 825));

		RetractablePlatform green1 = new RetractablePlatform(new Color(0, 128, 0), 350, 500, 675, 625);
		RetractablePlatform green2 = new RetractablePlatform(new Color(0, 128, 0), 600, 700, 224, 201);
		RetractablePlatform green3 = new RetractablePlatform(new Color(0, 128, 0), 850, 875, 550, 325);
		footholds.put(Byte.valueOf((byte) 26), green1);
		footholds.put(Byte.valueOf((byte) 27), green2);
		footholds.put(Byte.valueOf((byte) 28), green3);

		RetractablePlatform orange1 = new RetractablePlatform(new Color(255, 165, 0), 600, 700, 199, 176);
		RetractablePlatform orange2 = new RetractablePlatform(new Color(255, 165, 0), 1000, 1100, 600, 580);
		footholds.put(Byte.valueOf((byte) 29), orange1);
		footholds.put(Byte.valueOf((byte) 30), orange2);

		RetractablePlatform pink1 = new RetractablePlatform(new Color(255, 192, 203), 600, 700, 174, 151);
		RetractablePlatform pink2 = new RetractablePlatform(true, new Color(255, 192, 203), 925, 950, 550, 375);
		footholds.put(Byte.valueOf((byte) 31), pink1);
		footholds.put(Byte.valueOf((byte) 32), pink2);

		doors.add(green1);
		doors.add(green2);
		doors.add(green3);
		doors.add(orange1);
		doors.add(orange2);
		doors.add(pink1);
		doors.add(pink2);

		RetractablePlatform tan1 = new RetractablePlatform(new Color(210, 180, 140), 1901, 1933, 825, 660);
		RetractablePlatform tan2 = new RetractablePlatform(new Color(210, 180, 140), 2150, 2180, 825, 660);
		footholds.put(Byte.valueOf((byte) 33), tan1);
		footholds.put(Byte.valueOf((byte) 34), tan2);

		RetractablePlatform purple1 = new RetractablePlatform(new Color(128, 0, 128), 1935, 1966, 825, 660);
		RetractablePlatform purple2 = new RetractablePlatform(true, new Color(128, 0, 128), 2183, 2213, 825, 660);
		footholds.put(Byte.valueOf((byte) 35), purple1);
		footholds.put(Byte.valueOf((byte) 36), purple2);

		RetractablePlatform yellow1 = new RetractablePlatform(true, Color.YELLOW, 1967, 2000, 825, 660);
		RetractablePlatform yellow2 = new RetractablePlatform(Color.YELLOW, 2216, 2246, 825, 660);

		footholds.put(Byte.valueOf((byte) 37), yellow1);
		footholds.put(Byte.valueOf((byte) 38), yellow2);

		doors.add(tan1);
		doors.add(tan2);
		doors.add(purple1);
		doors.add(purple2);
		doors.add(yellow1);
		doors.add(yellow2);

		boxes.add(new BoxSpawnInfo(new Position(151, 0), .3f, .3f, .5f));
		boxes.add(new BoxSpawnInfo(new Position(221, 0), .3f, .3f, .5f));
		List<Switchable> greens = new ArrayList<Switchable>();
		greens.add(green1);
		greens.add(green2);
		greens.add(green3);
		List<Switchable> pinks = new ArrayList<Switchable>();
		pinks.add(pink1);
		pinks.add(pink2);
		List<Switchable> oranges = new ArrayList<Switchable>();
		oranges.add(orange1);
		oranges.add(orange2);

		List<Switchable> tans = new ArrayList<Switchable>();
		tans.add(tan1);
		tans.add(tan2);
		List<Switchable> purples = new ArrayList<Switchable>();
		purples.add(purple1);
		purples.add(purple2);
		List<Switchable> yellows = new ArrayList<Switchable>();
		yellows.add(yellow1);
		yellows.add(yellow2);

		switches.add(new SwitchSpawnInfo(new Color(255, 165, 0), new Position(0, 200), oranges));
		switches.add(new SwitchSpawnInfo(new Color(0, 128, 0), new Position(100, 200), greens));
		switches.add(new SwitchSpawnInfo(new Color(255, 192, 203), new Position(400, 500), pinks));

		switches.add(new SwitchSpawnInfo(new Color(210, 180, 140), new Position(1450, 660), tans));
		switches.add(new SwitchSpawnInfo(new Color(128, 0, 128), new Position(1375, 660), purples));
		switches.add(new SwitchSpawnInfo(Color.YELLOW, new Position(1300, 660), yellows));

		nBoxes.add(new NBoxSpawnInfo(new Position(1075, 1200)));

		return new LevelLayout(width, height, footholds, new Position(300, 0), new Position(2300, 660), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "mid2", "scrollingWindowBg", "mainBg", Double.POSITIVE_INFINITY);
	}

	private static LevelLayout constructMid2() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "level4", "mid2", "mid2", 3);
	}

	private static LevelLayout constructLevel4() {
		int width = 1500;
		int height = 1500;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		// ground platform
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		// left wall
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		// right wall
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		// top wall
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		// visible walls
		footholds.put(Byte.valueOf((byte) 4), new Platform(0, 50, 1500, 0));
		footholds.put(Byte.valueOf((byte) 5), new Platform(1450, 1500, 1500, 0));
		footholds.put(Byte.valueOf((byte) 6), new Platform(50, 1450, 1500, 1450));

		// 6 levels + bottom
		footholds.put(Byte.valueOf((byte) 10), new Platform(50, 1100, 1325, 1275));
		footholds.put(Byte.valueOf((byte) 11), new Platform(1200, 1450, 1325, 1275));

		footholds.put(Byte.valueOf((byte) 21), new Platform(150, 800, 1150, 1100));
		footholds.put(Byte.valueOf((byte) 22), new Platform(900, 1250, 1150, 1100));
		footholds.put(Byte.valueOf((byte) 23), new Platform(1350, 1450, 1150, 1100));

		footholds.put(Byte.valueOf((byte) 31), new Platform(50, 150, 975, 925));
		footholds.put(Byte.valueOf((byte) 32), new Platform(250, 425, 975, 925));
		footholds.put(Byte.valueOf((byte) 33), new Platform(525, 650, 975, 925));
		footholds.put(Byte.valueOf((byte) 34), new Platform(750, 1300, 975, 925));
		footholds.put(Byte.valueOf((byte) 35), new Platform(1400, 1450, 975, 925));

		footholds.put(Byte.valueOf((byte) 41), new Platform(150, 350, 800, 750));
		footholds.put(Byte.valueOf((byte) 42), new Platform(450, 900, 800, 750));
		footholds.put(Byte.valueOf((byte) 43), new Platform(1000, 1150, 800, 750));
		footholds.put(Byte.valueOf((byte) 44), new Platform(1250, 1450, 800, 750));

		footholds.put(Byte.valueOf((byte) 51), new Platform(50, 75, 625, 575));
		footholds.put(Byte.valueOf((byte) 52), new Platform(175, 450, 625, 575));
		footholds.put(Byte.valueOf((byte) 53), new Platform(550, 750, 625, 575));
		footholds.put(Byte.valueOf((byte) 54), new Platform(850, 1300, 625, 575));
		footholds.put(Byte.valueOf((byte) 55), new Platform(1400, 1450, 625, 575));
		RetractablePlatform green = new RetractablePlatform(new Color(143, 188, 139), 750, 850, 625, 575);
		footholds.put(Byte.valueOf((byte) 56), green);
		doors.add(green);

		footholds.put(Byte.valueOf((byte) 61), new Platform(50, 1050, 450, 400));

		RetractablePlatform darkCy = new RetractablePlatform(new Color(0, 139, 139), 1050, 1150, 450, 400);
		footholds.put(Byte.valueOf((byte) 62), darkCy);
		footholds.put(Byte.valueOf((byte) 63), new Platform(1150, 1450, 450, 400));

		doors.add(darkCy);

		footholds.put(Byte.valueOf((byte) 65), new Platform(50, 400, 275, 225));
		footholds.put(Byte.valueOf((byte) 66), new Platform(500, 1200, 275, 225));
		RetractablePlatform gold = new RetractablePlatform(new Color(255, 215, 0), 400, 500, 275, 225);
		footholds.put(Byte.valueOf((byte) 67), gold);

		switches.add(new SwitchSpawnInfo(new Position(700, 450), darkCy));
		switches.add(new SwitchSpawnInfo(new Position(1400, 450), green));
		switches.add(new SwitchSpawnInfo(new Position(1050, 625), gold));

		// separators
		footholds.put(Byte.valueOf((byte) 71), new Platform(500, 550, 1275, 1150));
		footholds.put(Byte.valueOf((byte) 72), new Platform(1100, 1150, 1100, 975));
		footholds.put(Byte.valueOf((byte) 73), new Platform(250, 300, 1100, 975));
		footholds.put(Byte.valueOf((byte) 74), new Platform(570, 610, 925, 800));
		footholds.put(Byte.valueOf((byte) 75), new Platform(300, 350, 925, 800));
		footholds.put(Byte.valueOf((byte) 76), new Platform(1100, 1150, 925, 800));
		footholds.put(Byte.valueOf((byte) 77), new Platform(1100, 1150, 750, 625));
		footholds.put(Byte.valueOf((byte) 78), new Platform(175, 225, 750, 625));
		footholds.put(Byte.valueOf((byte) 79), new Platform(650, 700, 750, 625));

		footholds.put(Byte.valueOf((byte) 81), new Platform(625, 675, 575, 450));
		footholds.put(Byte.valueOf((byte) 82), new Platform(1000, 1050, 575, 450));
		footholds.put(Byte.valueOf((byte) 83), new Platform(1150, 1200, 400, 275));

		boxes.add(new BoxSpawnInfo(new Position(350, 1159), .3f, .1f, .5f));
		boxes.add(new BoxSpawnInfo(new Position(575, 1159), .3f, .1f, .5f));

		return new LevelLayout(width, height, footholds, new Position(100, 1375), new Position(1250, 0), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "end1", "scrollingWindowBg", "mainBg", Double.POSITIVE_INFINITY);
	}

	private static LevelLayout constructEnd1() {
		int width = 1280;
		int height = 720;
		Map<Byte, Platform> footholds = new HashMap<Byte, Platform>();
		footholds.put(Byte.valueOf((byte) 0), new Platform(-99999, width, 0, -99999));
		footholds.put(Byte.valueOf((byte) 1), new Platform(-99999, 0, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 2), new Platform(width, width + 99999, height + 99999, -99999));
		footholds.put(Byte.valueOf((byte) 3), new Platform(-99999, width + 99999, height + 99999, height));

		List<BoxSpawnInfo> boxes = new ArrayList<BoxSpawnInfo>();
		List<RectangleSpawnInfo> rects = new ArrayList<RectangleSpawnInfo>();
		List<NBoxSpawnInfo> nBoxes = new ArrayList<NBoxSpawnInfo>();
		List<SwitchSpawnInfo> switches = new ArrayList<SwitchSpawnInfo>();
		List<OverlayInfo> tips = new ArrayList<OverlayInfo>();
		List<RetractablePlatform> doors = new ArrayList<RetractablePlatform>();

		return new LevelLayout(width, height, footholds, new Position(0, 0), new Position(1720, 800), -400, -400, boxes, rects, nBoxes, switches, tips, doors, "credits", "end1", "end1", 3);
	}

	public static void initialize() {
		loaded.put("intro1", constructIntro1());
		loaded.put("intro2", constructIntro2());
		loaded.put("intro3", constructIntro3());
		loaded.put("tutorial", constructTutorialLevel());
		loaded.put("mid1", constructMid1());
		loaded.put("level1", constructLevel1());
		loaded.put("level2", constructLevel2());
		loaded.put("level3", constructLevel3());
		loaded.put("mid2", constructMid2());
		loaded.put("level4", constructLevel4());
		loaded.put("end1", constructEnd1());
	}
}
