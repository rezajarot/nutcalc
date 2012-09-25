
#ifndef GameDefine_H
#define GameDefine_H

#define PI 3.141592

typedef enum {
	FAN_DIR_LEFT = 0,
	FAN_DIR_RIGHT,
} FanDir;

#define ANIMATION_DURATION_SECONDS	.1
#define SCORE_ANIMATION_SECONDS .5

#define BALL_IPHONE_OBJECT_COUNT 8
#define BALL_IPAD_OBJECT_COUNT 16

#define BALL_ANIMATION_INTERVAL 30	
#define FAN_ANIMATION_INTERVAL 30

#define BALL_ARROW_LENGTH_IPAD 120
#define BALL_ARROW_LENGTH_IPHONE 60
#define _SOUND_ON

extern NSInteger gnScoreEasy;
extern NSInteger gnScoreMedium;
extern NSInteger gnScoreHard;

#endif

