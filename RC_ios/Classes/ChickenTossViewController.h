
#import "EasyViewController.h"
#import "MediumViewController.h"
#import "HardViewController.h"
#import "TrackMaker.h"

#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioToolbox.h>

@class ChickenTossAppDelegate;

@interface ChickenTossViewController : UIViewController <UINavigationControllerDelegate,
														EasyViewControllerDelegate,
														MediumViewControllerDelegate,
														HardViewControllerDelegate>
{
	IBOutlet EasyViewController *easyController;
	IBOutlet MediumViewController *mediumController;
	IBOutlet HardViewController *hardController;
	
	IBOutlet UIButton *btEasy;
	IBOutlet UIButton *btMedium;
	IBOutlet UIButton *btHard;
	IBOutlet UIButton *btSound;
	
	SystemSoundID m_soundBounceOut;
	SystemSoundID m_soundEasyBounceIn;
	SystemSoundID m_soundEasyMiss;
	SystemSoundID m_soundMediumBounceIn;
	SystemSoundID m_soundMediumMiss;
	SystemSoundID m_soundHardBounceIn;
	SystemSoundID m_soundHardMiss;
	
	BOOL m_fSound;	
	
	int easyScore;
	int mediumScore;
	int hardScore;
	
	TrackMaker* m_TrackMaker;
	
	ChickenTossAppDelegate *app;
}

-(IBAction)onEasy:(id)sender;
-(IBAction)onMedium:(id)sender;
-(IBAction)onHard:(id)sender;
-(IBAction)onQuit:(id)sender;
-(IBAction)onSoundOnOff:(id)sender;

-(void)playBounceOut;
-(void)playEasyBounceIn;
-(void)playEasyMiss;
-(void)playMediumBounceIn;
-(void)playMediumMiss;
-(void)playHardBounceIn;
-(void)playHardMiss;

@property (nonatomic, retain) ChickenTossAppDelegate *app;

@end
