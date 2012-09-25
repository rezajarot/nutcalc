
#import "ChickenTossViewController.h"
#import "ChickenTossAppDelegate.h"

@implementation ChickenTossViewController

@synthesize app;

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.

- (void)viewDidLoad {
	m_fSound = TRUE;
	
	NSString * szMenuSound;
	NSURL *urlMenuSound;
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"BounceOut" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundBounceOut);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Easy_BounceIn" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundEasyBounceIn);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Easy_Miss" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundEasyMiss);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Medium_BounceIn" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundMediumBounceIn);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Medium_Miss" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundMediumMiss);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Hard_BounceIn" ofType:@"wav"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundHardBounceIn);
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Hard_Miss" ofType:@"wav"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	AudioServicesCreateSystemSoundID((CFURLRef)urlMenuSound, &m_soundHardMiss);
	
	[btSound.titleLabel setFont:[UIFont fontWithName:@"Berthold City" size:16.0]];

	easyScore = 0;
	mediumScore = 0;
	hardScore = 0;
	
	m_TrackMaker = new TrackMaker;
	
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
		m_TrackMaker->SetMachine(MACHINE_IPHONE);		
	} else {
		m_TrackMaker->SetMachine(MACHINE_IPAD);
	}

    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreEasy forKey:@"ScoreEasy"];
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreMedium forKey:@"ScoreMedium"];
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreHard forKey:@"ScoreHard"];
}

- (void)dealloc {
	AudioServicesDisposeSystemSoundID(m_soundBounceOut);
	AudioServicesDisposeSystemSoundID(m_soundEasyBounceIn);
	AudioServicesDisposeSystemSoundID(m_soundEasyMiss);
	AudioServicesDisposeSystemSoundID(m_soundMediumBounceIn);
	AudioServicesDisposeSystemSoundID(m_soundMediumMiss);
	AudioServicesDisposeSystemSoundID(m_soundHardBounceIn);
	AudioServicesDisposeSystemSoundID(m_soundHardMiss);
	
	if (m_TrackMaker)
		delete m_TrackMaker;
	
    [super dealloc];
}


#pragma mark CALLBACK

- (void)checkMainMenu:(UIViewController *)controller gainScore:(NSInteger)score
{
	if (controller == easyController)
		easyScore = score;
	if (controller == mediumController)
		mediumScore = score;
	if (controller == hardController)
		hardScore = score;
	
	[self dismissModalViewControllerAnimated:YES];
}


-(void) checkplayBounceOut{
	[self playBounceOut];
}

-(void) checkplayEasyBounceIn{
	[self playEasyBounceIn];
}

-(void) checkplayEasyMiss{
	[self playEasyMiss];
}

-(void) checkplayMediumBounceIn{
	[self playMediumBounceIn];
}

-(void) checkplayMediumMiss{
	[self playMediumMiss];
}

-(void) checkplayHardBounceIn{
	[self playHardBounceIn];
}

-(void) checkplayHardMiss{
	[self playHardMiss];
}


#pragma mark IBAction

-(IBAction)onEasy:(id)sender
{
	easyController.delegate = self;
	
	[easyController setSound:m_fSound otherTracker:m_TrackMaker];
	
	[easyController setModalTransitionStyle: UIModalTransitionStyleFlipHorizontal];
	[self presentModalViewController:easyController animated:YES];
	
	[easyController initGame];
}

-(IBAction)onMedium:(id)sender
{
	mediumController.delegate = self;
	
	[mediumController setSound:m_fSound otherTracker:m_TrackMaker];
	
	[mediumController setModalTransitionStyle: UIModalTransitionStyleFlipHorizontal];
	[self presentModalViewController:mediumController animated:YES];
	
	[mediumController initGame];
}


-(IBAction)onHard:(id)sender
{
	hardController.delegate = self;
	
	[hardController setSound:m_fSound otherTracker:m_TrackMaker];
	
	[hardController setModalTransitionStyle: UIModalTransitionStyleFlipHorizontal];
	[self presentModalViewController:hardController animated:YES];
	
	[hardController initGame];
}


-(IBAction)onQuit:(id)sender
{
	NSUserDefaults *prefs = [NSUserDefaults standardUserDefaults];
    [prefs setInteger:gnScoreEasy forKey:@"ScoreEasy"];
    [prefs setInteger:gnScoreMedium forKey:@"ScoreMedium"];
    [prefs setInteger:gnScoreHard forKey:@"ScoreHard"];
	[prefs synchronize];
	
	exit(0);
}

-(IBAction)onSoundOnOff:(id)sender
{
	m_fSound = m_fSound == TRUE ? FALSE : TRUE;
	
	if (m_fSound)
	{
		NSString *str = [NSString stringWithFormat: @"Sound: On"];
		
		[btSound setTitle:str forState:UIControlStateNormal];
	}
	else
	{
		NSString *str = [NSString stringWithFormat: @"Sound: Off"];
		
		[btSound setTitle:str forState:UIControlStateNormal];
	}
}

#pragma mark Sound


-(void)playBounceOut{
	AudioServicesPlaySystemSound(m_soundBounceOut);
}

-(void)playEasyBounceIn{
	AudioServicesPlaySystemSound(m_soundEasyBounceIn);
}

-(void)playEasyMiss{
	AudioServicesPlaySystemSound(m_soundEasyMiss);
}

-(void)playMediumBounceIn{
	AudioServicesPlaySystemSound(m_soundMediumBounceIn);
}

-(void)playMediumMiss{
	AudioServicesPlaySystemSound(m_soundMediumMiss);
}

-(void)playHardBounceIn{
	AudioServicesPlaySystemSound(m_soundHardBounceIn);
}

-(void)playHardMiss{
	AudioServicesPlaySystemSound(m_soundHardMiss);
}

@end
