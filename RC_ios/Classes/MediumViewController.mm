
#import "MediumViewController.h"
#import "ChickenTossAppDelegate.h"

@implementation MediumViewController

@synthesize delegate;

- (void)viewDidLoad {
	m_TrackMaker->SetLevel(LV_2);
	
	LevelInformation* pInfo = m_TrackMaker->GetLevelInfo();
	
	CGPoint pt = CGPointMake(pInfo->nPaperInitX, pInfo->nPaperInitY);
	
	[ball setCenter:pt];
	
	[score setFont:[UIFont fontWithName:@"Berthold City" size:16.0]];
	[best setFont:[UIFont fontWithName:@"Berthold City" size:16.0]];
	[windValue setFont:[UIFont fontWithName:@"Berthold City" size:20.0]];
	
	m_initballRect = [ball frame];
	m_arrowPoint = toTrough.center;
	
	m_nScore = 0;
	m_nBest = gnScoreMedium;
	NSString* str = [NSString stringWithFormat:@"%d", m_nBest];
	[best setText:str];
	
	m_fSubmit = FALSE;
	
	[super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	//AudioServicesDisposeSystemSoundID(m_BackSound);
	
	[self stopAnimation];
	//[self fanstopAnimation];
	
#ifdef _SOUND_ON
	if (player != nil)
	{
		[player release];
		player = nil;
	}
#endif
}

- (void)dealloc {
    [super dealloc];
}

#pragma mark IBAction
-(IBAction)returnMainMenu:(id)sender
{
    if ([delegate respondsToSelector:@selector(checkMainMenu:gainScore:)])
	{
		[delegate checkMainMenu:self gainScore: m_nBest];
	}
	[self viewDidUnload];
}

#pragma mark FUNCTION
-(void) reset{
	[self initGame];
}

-(void) rotateView:(UIView*)view Alpha:(CGFloat)value{
	[UIView beginAnimations:nil context:nil];
	[UIView setAnimationDuration:ANIMATION_DURATION_SECONDS];
	view.transform = CGAffineTransformMakeRotation(value);
	[UIView commitAnimations];
    
    if (m_nFanDir == FAN_DIR_RIGHT) {
        windDirect.image = [UIImage imageNamed: @"breeze_dark_L.png"];
    } else {
        windDirect.image = [UIImage imageNamed: @"breeze_dark_R.png"];
    }
}

-(void) rotateView:(UIView*)view Alpha:(CGFloat)value Center:(CGPoint)point{
	[UIView beginAnimations:nil context:nil];
	[UIView setAnimationDuration:ANIMATION_DURATION_SECONDS];
	view.transform = CGAffineTransformMakeRotation(value);
	[UIView commitAnimations];
	view.center = point;
    
    if (m_nFanDir == FAN_DIR_RIGHT) {
        windDirect.image = [UIImage imageNamed: @"breeze_dark_L.png"];
    } else {
        windDirect.image = [UIImage imageNamed: @"breeze_dark_R.png"];
    }
}

- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
	UITouch *touch = [touches anyObject];
	
	CGPoint touchPoint= [touch locationInView:self.view];
	
	CGRect rt = submit.frame;
	
	if (CGRectContainsPoint(rt, touchPoint) && m_fSubmit)
	{
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Submit Score?" 
														message:@"Would you like to submit your best score for this level to the online high score board?"
													   delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
		[alert show];
		[alert release];
	}
	if (CGRectContainsPoint(ball.frame, touchPoint))
	{
		m_fTouchDown = TRUE;
		m_StartTouchPoint = touchPoint;
	}
}

- (void) touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
	UITouch *touch = [touches anyObject];
	
	if (!m_fTouchDown || m_fMove)
		return;
	
	CGPoint touchPoint = [touch locationInView:self.view];
	
	m_EndTouchPoint = touchPoint;
	
	if (CGPointEqualToPoint(m_StartTouchPoint, m_EndTouchPoint))
	{
		m_fMove = FALSE;
	}
	else
	{
		BOOL fSuccess = [self calcAlpha];
		
		if (!fSuccess)
		{
			m_fTouchDown = FALSE;
			return;
		}
		
		if (!m_TrackMaker->SetThrowAngle(m_rAlpha))
		{
			m_fTouchDown = FALSE;
			return;
		}
		
		m_fMove = TRUE;
		[self dispArrow];
		
		m_animationInterval = 1.0 / BALL_ANIMATION_INTERVAL;		
		[self setAnimationInterval: m_animationInterval];
	}
	
	m_fTouchDown = FALSE;
}

#ifdef _SOUND_ON
- (void) audioPlayerDidFinishPlaying: (AVAudioPlayer *) theplayer 
						successfully:(BOOL)flag { 
	if (player != nil)
		[player play];
}
#endif

-(void) loadSound{
#ifdef _SOUND_ON
	NSString * szMenuSound = [[NSBundle mainBundle] pathForResource:@"Level_Medium_Background" ofType:@"mp3"];
	NSURL *urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	
	NSError *error = nil;
	player = [[AVAudioPlayer alloc] initWithContentsOfURL: urlMenuSound error:&error];
	player.delegate = self;
	
	if (error != NULL)
	{
		[error release];
	}
	if (player != nil)
		[player play];
	
	szMenuSound = [[NSBundle mainBundle] pathForResource:@"DogBark2Loud" ofType:@"mp3"];
	urlMenuSound = [NSURL fileURLWithPath:szMenuSound];
	error = nil;
	player_bonus = [[AVAudioPlayer alloc] initWithContentsOfURL:urlMenuSound error:&error];
	player_bonus.delegate = self;
#endif
}

-(void) setSound:(BOOL)fFlag otherTracker: (TrackMaker*)tracker{
	m_fSound = fFlag;
	m_TrackMaker = tracker;
	
	if (m_fSound)
		[self loadSound];
}

-(void) initGame{
	[self stopAnimation];
	
	[self setFanDir];
	[self setFanV];
	
	//[windValue setFont:[UIFont fontWithName:@"Berthold City" size:12 + 1.5 * m_rFan_A]];
	
	m_TrackMaker->SetLevel(LV_2);
	
	int nDirect;
	nDirect = m_nFanDir == FAN_DIR_LEFT ? 1 : -1;
	
	m_TrackMaker->SetWindSpeed(nDirect, m_rFan_A);
	
	m_nBallIndex = 0;
	
	[toTrough setCenter:m_arrowPoint];
	[self rotateView:toTrough Alpha:0];
	
	[ball setFrame: m_initballRect];
	
	ChickenTossAppDelegate* del = [UIApplication sharedApplication].delegate;
	
	ball.image = del.m_img1;
	
	m_fTouchDown = FALSE;
	m_fMove = FALSE;
	m_fGoal = FALSE;
	
	m_nFrameNum = 0;
	
	recycled.hidden = NO;
}

-(void) startAnimation{
	m_animationTimer = [NSTimer scheduledTimerWithTimeInterval:m_animationInterval target:self selector:@selector(onTimer:) userInfo:nil repeats:YES];		
}

-(void) stopAnimation{
	[m_animationTimer invalidate];
	m_animationTimer = nil;
}

-(void) setAnimationInterval:(NSTimeInterval)interval{
	m_animationInterval = interval;
    
	if (m_animationTimer != nil)
        [self stopAnimation];
	
    [self startAnimation];
}

-(void) onTimer:(id)sender{
	[self startMove];
}

-(void) startMove{
	if (!m_fMove)
		return;
	
	m_nFrameNum++;
	
	if (m_nFrameNum == 1)
	{
		//[self.view bringSubviewToFront:frontView];
		//[self.view bringSubviewToFront:frontView1];
		
		//[self.view bringSubviewToFront:fanLeft];
		//[self.view bringSubviewToFront:fanLeft_1];
		//[self.view bringSubviewToFront:fanRight];
		//[self.view bringSubviewToFront:fanRight_1];
		
		[self.view bringSubviewToFront:ball];
		
		[self.view bringSubviewToFront:mainMenu];
	}
	
	if (m_nFrameNum == UP_FRAME_NUM)
	{
		recycled.hidden  = NO;
		
		[self.view bringSubviewToFront:ball];
		
		//[self.view bringSubviewToFront:frontView];
		//[self.view bringSubviewToFront:frontView1];
		
		//[self.view bringSubviewToFront:fanLeft];
		//[self.view bringSubviewToFront:fanLeft_1];
		//[self.view bringSubviewToFront:fanRight];
		//[self.view bringSubviewToFront:fanRight_1];
		
		[self.view bringSubviewToFront:recycled];
		
		[self.view bringSubviewToFront:mainMenu];
	}
	
	int nState;
	m_TrackMaker->GetTrack(m_nFrameNum, m_nCenterX, m_nCenterY, m_rScale, m_nBallIndex, nState);
	
	if (m_fSound)
	{
		switch (nState) {
		case STATE_IN:
			[delegate checkplayMediumBounceIn];
			break;
		case STATE_SIDE_IN:
			[delegate checkplayBounceOut];
			break;
		case STATE_RING_IN:
			[delegate checkplayBounceOut];
			break;
		case STATE_RING_OUT:
			[delegate checkplayBounceOut];
			break;
		case STATE_OUT:
			[delegate checkplayMediumMiss];
			break;
		default:
			break;
		}
	}
	
	if (m_TrackMaker->GetSuccess())
	{
		m_fGoal = TRUE;
	}
	
	if (m_nFrameNum < m_TrackMaker->GetRealFrameNum())
	{
		[self calcBallInfo];			
	}
	
	int nScoreChange;
	
	if (m_nFrameNum == m_TrackMaker->GetRealFrameNum())
	{
		nScoreChange = m_nScore;
		
		if (m_fGoal)
		{
			m_nScore ++;
			m_nBest = m_nScore > m_nBest ? m_nBest + 1 : m_nBest;
			gnScoreMedium = m_nBest;
			
			m_fSubmit= TRUE;
		}
		else
		{
			m_nScore = 0;
		}
		
		if (m_fSubmit && (m_nScore != nScoreChange))
			[self UpdateScore];
		
		[self initGame];
	}
}

-(BOOL) calcAlpha{
	LevelInformation* pInfo = m_TrackMaker->GetLevelInfo();
	CGPoint pt = CGPointMake(pInfo->nPaperInitX, pInfo->nPaperInitY);
	
	float rw = m_EndTouchPoint.x - pt.x;
	float rh = m_EndTouchPoint.y - pt.y;
	
	if (rh >= 0)
		return FALSE;
	
	rh = -rh;
	m_rAlpha = atan2f(rh, rw);
	
	return TRUE;
}

-(void) calcBallInfo{
	CGRect rt;
	int nWidth;
	
	[ball setCenter: CGPointMake(m_nCenterX, m_nCenterY)];
	
	rt = [ball frame];
	
	nWidth = m_initballRect.size.width * m_rScale;
	
	rt.size.width = nWidth;
	rt.size.height = nWidth;
	
	[ball setFrame:rt];
	ChickenTossAppDelegate* del = [UIApplication sharedApplication].delegate;
	
	int i = m_nBallIndex + 1;
	
	switch (i) {
	case 1:
		ball.image = del.m_img1;
		break;
	case 2:
		ball.image = del.m_img2;
		break;
	case 3:
		ball.image = del.m_img3;
		break;
	case 4:
		ball.image = del.m_img4;
		break;
	case 5:
		ball.image = del.m_img5;
		break;
	case 6:
		ball.image = del.m_img6;
		break;
	case 7:
		ball.image = del.m_img7;
		break;
	case 8:
		ball.image = del.m_img8;
		break;
	case 9:
		ball.image = del.m_img9;
		break;
	case 10:
		ball.image = del.m_img10;
		break;
	case 11:
		ball.image = del.m_img11;
		break;
	case 12:
		ball.image = del.m_img12;
		break;
	case 13:
		ball.image = del.m_img13;
		break;
	case 14:
		ball.image = del.m_img14;
		break;
	case 15:
		ball.image = del.m_img15;
		break;
	case 16:
		ball.image = del.m_img16;
		break;
	default:
		break;
	}
}

-(void) setFanDir{
	int nTemp= rand() % 2;
	
	if (nTemp == FAN_DIR_LEFT)
	{
		m_nFanDir = FAN_DIR_LEFT;
		[self rotateView:windDirect Alpha: 0];
	}
	else
	{
		m_nFanDir = FAN_DIR_RIGHT;
		[self rotateView:windDirect Alpha: 0];
	}
}

-(void) setFanV{
	int i = rand() % 6;	
	int j = rand() % 100;
	
	//i = 0;
	//j = 0;
	
	float rTemp = j / 100.0;
	
	m_rFan_A = i + rTemp;
	
	//m_rFan_A == m_rFan_A < 0.01 ? 0.01 : m_rFan_A;
	
	m_fanInterval = 1 / FAN_ANIMATION_INTERVAL;
	m_nFanIndex = 0;
	
	//[self fansetAnimationInterval:m_fanInterval];
	[self dispWindValue];	
	
	m_nFanIndex = 0;
}

-(void) dispArrow{
	float x, y;
	
	if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
	{
		x = CGRectGetMidX(m_initballRect) + BALL_ARROW_LENGTH_IPHONE * cos(m_rAlpha); 
		y = CGRectGetMidY(m_initballRect) - BALL_ARROW_LENGTH_IPHONE * sin(m_rAlpha);
	}
	else
	{
		x = CGRectGetMidX(m_initballRect) + BALL_ARROW_LENGTH_IPAD * cos(m_rAlpha); 
		y = CGRectGetMidY(m_initballRect) - BALL_ARROW_LENGTH_IPAD * sin(m_rAlpha);
	}
	
	if (m_rAlpha >= 0)
		[self rotateView:toTrough Alpha: (PI / 2 - m_rAlpha) Center: CGPointMake(x, y)];
	else
		[self rotateView:toTrough Alpha: (PI / 2 + m_rAlpha) Center: CGPointMake(x, y)];
}

-(void) dispWindValue{
	//windValue.textColor = [UIColor blackColor];
	
	NSString *str = [NSString stringWithFormat: @"%.2f", m_rFan_A];
	windValue.text= str;
}

-(void) animationView:(UIView*)view initFrame:(CGRect)sFrame endFrame:(CGRect)eFrame
{
	[view setFrame:sFrame];
	[UIView beginAnimations:nil context:nil];
	[UIView setAnimationDuration:SCORE_ANIMATION_SECONDS];
	[view setFrame:eFrame];
	[UIView commitAnimations];
}

-(void) destroyMovedScore:(id)sender{
	UILabel *label = (UILabel *)sender;
	[score setText:[label text]];
	[label removeFromSuperview];
}

-(void) destroyMovedBest:(id)sender{
	UILabel *label = (UILabel *)sender;
	[best setText:[label text]];
	[label removeFromSuperview];
}

-(void) destroyMovedSubmit:(id)sender{
	UILabel *label = (UILabel *)sender;	
	[submit setTextColor:label.textColor];
	[submit setText:[label text]];
	[label removeFromSuperview];
}

-(void) UpdateScore
{
	NSString* str;
	
	if (m_nScore == 5)
	{
		[player_bonus play];
	}
	
	if (m_fSubmit && m_nBest == 1)
	{
		str = [NSString stringWithFormat:@"%d", m_nScore];
		[score setText:str];
		
		str = [NSString stringWithFormat:@"%d", m_nBest];
		[best setText:str];
	}
	else
	{
		[self.view addSubview:moveScore];		
		
		str = [NSString stringWithFormat:@"%d", m_nScore];
		[score setText:str];
		
		if (m_nScore == m_nBest)
		{
			[self.view addSubview:moveBest];
			
			str = [NSString stringWithFormat:@"%d", m_nBest];
			[best setText:str];
		}
	}
}

@end
