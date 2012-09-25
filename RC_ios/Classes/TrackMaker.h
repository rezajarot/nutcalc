
#if !defined(_TYPE_H_)

#define _TYPE_H_

#define UP_FRAME_NUM	22
#define DOWN_FRAME_NUM	27
#define DROP_FRAME_NUM	5
#define BACK_FRAME_NUM	20
#define BACK_HALF_FRAME_NUM	9
#define RING_OUT_FRAME_NUM	22
#define TOP_FRAME_NUM	4
#define BACK_FROM_FLOOR_FRAME_NUM	4
#define STOP_FRAME_NUM	16

#define TOTAL_FRAME_NUM  UP_FRAME_NUM + DOWN_FRAME_NUM + DROP_FRAME_NUM + RING_OUT_FRAME_NUM + TOP_FRAME_NUM + STOP_FRAME_NUM

#define IPHONE_WIDTH	320
#define IPHONE_HEIGHT	480
#define IPAD_WIDTH		768
#define IPAD_HEIGHT		1024

#define IPHONE_ROUND_LIMIT	8
#define IPAD_ROUND_LIMIT	16

#define SCALEX_IPHONE_IPAD	IPAD_WIDTH/IPHONE_WIDTH
#define SCALEY_IPHONE_IPAD	IPAD_HEIGHT/IPHONE_HEIGHT

#define UPTIME	1
#define DOWNTIME	1
#define DROPTIME	0.3
#define BACKTIME	0.3
#define MAX_WIND	6

enum
{
	LV_1,
	LV_2,
	LV_3,
	LV_NUM
};

enum
{
	STATE_NONE,
	STATE_IN,
	STATE_SIDE_IN,
	STATE_RING_IN,
	STATE_RING_OUT,
	STATE_OUT,
	STATE_BONUS
};

enum
{
	MACHINE_IPHONE,
	MACHINE_IPAD,
};

struct LevelInformation
{
	int nPaperInitX;
	int nPaperInitY;
	
	int nBinBottomY;
	int nBinTopY;
	int nBinCenterX;
	int nBinWidth;
	
	int nUpFrameNum;
	int nDownFrameNum;
	int nDropFrameNum;
	int nBackFrameNum;
	
	int nFlyRight;
	int nFlyFloor;
	int nFlyTop;
	int nFlyBackTop;
	int nFlyBackFromFloor;
	
	float rInitScale;
	float rTopScale;
	float rLastScale;
	
	int nStartAngle;
	int nEndAngle;
};

class TrackMaker
{
public:
	TrackMaker();
	~TrackMaker();
	
	void SetLevel(int nLevelNum);
	void SetMachine(int nMachine);
	int SetThrowAngle(float rThrowAngle);
	void SetWindSpeed(int nDirect, float rSpeed);
	bool GetTrack(int nFrameNum, int& nPaperX, int &nPaperY, float& rScale, int& nRotate, int& nState);
	int	GetRealFrameNum();
	bool GetSuccess();
	LevelInformation* GetLevelInfo();
	
protected:
	void CalcDropTrack(int& nPaperX, int& nPaperY, float& rScale);
	void CalcBackTrack(int& nPaperX, int& nPaperY, float& rScale, int nMode);
	void CalcRingOutTrack(int& nPaperX, int& nPaperY, float& rScale, int nMode);
	void CalcTrack();
	void AddRoundIndex(int &nRoundIndex);
	
private:
	LevelInformation m_LevelInfo;
	
	int		m_nTrackX[TOTAL_FRAME_NUM];
	int		m_nTrackY[TOTAL_FRAME_NUM];
	float	m_rScale[TOTAL_FRAME_NUM];
	int		m_nState[TOTAL_FRAME_NUM];
	int		m_nRound[TOTAL_FRAME_NUM];
	bool	m_fSuccess;
	float	m_rThrowAngle;
	int		m_nWindDrect;
	float	m_rWindSpeed;
	int		m_nRealFrameNum;
	int		m_nCurrentFrameIndex;
	int		m_nCurrentRoundIndex;
	int		m_nMachine;
};

#endif
