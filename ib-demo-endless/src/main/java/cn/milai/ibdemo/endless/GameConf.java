package cn.milai.ibdemo.endless;

/**
 * 无尽模式-常量
 * @author milai
 * @date 2022.05.03
 */
public class GameConf {

	static final String TITLE_PREFIX = "敌星弹雨 - 无尽模式";
	static final String AUDIO_BG_FILE = "/audio/bg.mp3";
	static final String IMG_BACKGROUD_FILE = "/img/backgroud.gif";
	static final String AUDIO_BOMB_FILE = "/audio/bomb.mp3";
	/**
	 * 默认宽度
	 */
	static final int WIDTH = 554;
	/**
	 * 默认高度
	 */
	static final int HEIGHT = 689;
	static final int INIT_LEVEL_UP_GAME_SCORE = 30;
	static final int MAX_PLAYER_BULLET_NUM = 10;
	static final int INIT_MAX_ENEMY_NUM = 5;
	static final int LEVEL_UP_SCORE_INTERVAL = 10;
	public static final int GAME_OVER_LABEL_POS_X = 280;
	public static final int GAME_OVER_LABEL_POS_Y = 216;
	public static final int RESTART_BUTTON_POS_X = 273;
	public static final int RESTART_BUTTON_POS_Y = 403;
	/**
	 * 最短添加新敌机的时间间隔
	 */
	static final long MIN_ADD_ENEMEY_FRAMES = 1;
	/**
	 * 初始添加新敌机的时间间隔
	 */
	static final int INIT_ADD_NORMAL_ENEMEY_FRAMES = 20;
	static final long LEVEL_UP_FRAMES = 200;
	static final long ADD_LADDER_WELCOME_PLANE_FRAMES = 24;
	static final long ADD_VERTICAL_WELCOME_PLANE_FRAMES = 16;
	static final double ADD_ENEMY_CHANCE = 0.2;

}
