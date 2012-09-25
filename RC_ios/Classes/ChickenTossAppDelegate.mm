
#import "ChickenTossAppDelegate.h"
#import "ChickenTossViewController.h"


NSInteger gnScoreEasy;
NSInteger gnScoreMedium;
NSInteger gnScoreHard;


@implementation ChickenTossAppDelegate

@synthesize window;

@synthesize viewController;

@synthesize m_img1;
@synthesize m_img2;
@synthesize m_img3;
@synthesize m_img4;
@synthesize m_img5;
@synthesize m_img6;
@synthesize m_img7;
@synthesize m_img8;
@synthesize m_img9;
@synthesize m_img10;
@synthesize m_img11;
@synthesize m_img12;
@synthesize m_img13;
@synthesize m_img14;
@synthesize m_img15;
@synthesize m_img16;

- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
	viewController.app = self;
	
    [self loadInfo]; 
	
	NSString* str;
	
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
	{
		str = [[NSBundle mainBundle] pathForResource: @"chicken_01.png" ofType: nil];
		m_img1 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_02.png" ofType: nil];
		m_img2 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_03.png" ofType: nil];
		m_img3 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_04.png" ofType: nil];
		m_img4 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_05.png" ofType: nil];
		m_img5 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_06.png" ofType: nil];
		m_img6 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_07.png" ofType: nil];
		m_img7 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"chicken_08.png" ofType: nil];
		m_img8 = [[UIImage alloc] initWithContentsOfFile:str];
	}
	else
	{
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper1.png" ofType: nil];
		m_img1 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper2.png" ofType: nil];
		m_img2 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper3.png" ofType: nil];
		m_img3 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper4.png" ofType: nil];
		m_img4 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper5.png" ofType: nil];
		m_img5 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper6.png" ofType: nil];
		m_img6 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper7.png" ofType: nil];
		m_img7 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper8.png" ofType: nil];
		m_img8 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper9.png" ofType: nil];
		m_img9 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper10.png" ofType: nil];
		m_img10 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper11.png" ofType: nil];
		m_img11 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper12.png" ofType: nil];
		m_img12 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper13.png" ofType: nil];
		m_img13 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper14.png" ofType: nil];
		m_img14 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper15.png" ofType: nil];
		m_img15 = [[UIImage alloc] initWithContentsOfFile:str];
		
		str = [[NSBundle mainBundle] pathForResource: @"pad_paper16.png" ofType: nil];
		m_img16 = [[UIImage alloc] initWithContentsOfFile:str];
	}
	
	[window addSubview:viewController.view];
	//[window makeKeyAndVisible];
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
	[self saveInfo];
}

- (void)applicationWillTerminate:(UIApplication *)application
{
	[self saveInfo];
}

- (void)dealloc {
	[m_img1 release];
	[m_img2 release];
	[m_img3 release];
	[m_img4 release];
	[m_img5 release];
	[m_img6 release];
	[m_img7 release];
	[m_img8 release];
	
	if (UI_USER_INTERFACE_IDIOM() != UIUserInterfaceIdiomPhone)
	{
		[m_img9 release];
		[m_img10 release];
		[m_img11 release];
		[m_img12 release];
		[m_img13 release];
		[m_img14 release];
		[m_img15 release];
		[m_img16 release];		
	}
	
	[viewController release];
    [window release];
    [super dealloc];
}

- (void)loadInfo
{
    gnScoreEasy = [[NSUserDefaults standardUserDefaults] integerForKey:@"ScoreEasy"];
	gnScoreMedium = [[NSUserDefaults standardUserDefaults] integerForKey:@"ScoreMedium"];
	gnScoreHard = [[NSUserDefaults standardUserDefaults] integerForKey:@"ScoreHard"];
}

- (void)saveInfo
{
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreEasy forKey:@"ScoreEasy"];
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreMedium forKey:@"ScoreMedium"];
    [[NSUserDefaults standardUserDefaults] setInteger:gnScoreHard forKey:@"ScoreHard"];
}

@end
