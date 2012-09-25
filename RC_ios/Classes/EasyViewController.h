
@protocol EasyViewControllerDelegate;

#import "GameDefine.h"
#import "TrackMaker.h"

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface EasyViewController : UIViewController<AVAudioPlayerDelegate>{	
	id <EasyViewControllerDelegate> delegate;
	
	IBOutlet UIImageView *initView;
	IBOutlet UIImageView *initView1;
	IBOutlet UIImageView *initView2;
	
	IBOutlet UIImageView *ball;
	
	IBOutlet UIButton *mainMenu;
	
	IBOutlet UIImageView *toTrough;
	IBOutlet UIImageView *windDirect;
	IBOutlet UIImageView *recycled;
	
	IBOutlet UILabel* windValue;
	
	IBOutlet UILabel* score;
	IBOutlet UILabel* best;
	IBOutlet UILabel* submit;
	
	IBOutlet UILabel* moveScore;
	IBOutlet UILabel* moveBest;
	IBOutlet UILabel* moveSubmit;

	AVAudioPlayer* player;
	AVAudioPlayer* player_bonus;
	
	TrackMaker* m_TrackMaker;
	
	CGPoint m_StartTouchPoint;
	CGPoint m_EndTouchPoint;
	
	NSTimer* m_animationTimer;
	NSTimeInterval m_animationInterval;
	
	NSTimer* m_fanTimer;
	NSTimeInterval m_fanInterval;
	
	CGRect m_initballRect;
	CGPoint m_arrowPoint;
	
	int m_nBallIndex;
	int m_nFanIndex;
	
	FanDir m_nFanDir;
	
	int m_nCenterX;
	int m_nCenterY;
	
	float m_rAlpha;
	float m_rScale;
	float m_rFan_A;
	
	int m_nFrameNum;
	
	BOOL m_fTouchDown;
	BOOL m_fMove;
	BOOL m_fSubmit;
	BOOL m_fGoal;
	BOOL m_fSound;
	BOOL m_fTotorial;
	
	int m_nScore;
	int m_nBest;
}

@property (nonatomic, assign) id <EasyViewControllerDelegate> delegate;

-(IBAction) returnMainMenu:(id)sender;

-(void) initGame;
-(void) loadSound;

-(void) setSound:(BOOL)fFlag otherTracker: (TrackMaker*)tracker;

-(void) rotateView:(UIView*)view Alpha:(CGFloat)value;
-(void) rotateView:(UIView*)view Alpha:(CGFloat)value Center:(CGPoint)point;

-(void) dispArrow;
-(void) dispWindValue;

-(void) startAnimation;
-(void) stopAnimation;
-(void) setAnimationInterval:(NSTimeInterval)interval;

-(void) startMove;

-(BOOL) calcAlpha;
-(void) calcBallInfo;

-(void) setFanDir;
-(void) setFanV;

-(void) reset;

-(void) animationView:(UIView*)view initFrame:(CGRect)sFrame endFrame:(CGRect)eFrame;
-(void) UpdateScore;

@end

@protocol EasyViewControllerDelegate<NSObject>
@optional

-(void)checkMainMenu:(UIViewController *)controller gainScore:(NSInteger)score;

-(void) checkplayBounceOut;
-(void) checkplayEasyBounceIn;
-(void) checkplayEasyMiss;

@end