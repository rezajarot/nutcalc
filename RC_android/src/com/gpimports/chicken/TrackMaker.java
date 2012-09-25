package com.gpimports.chicken;

public class TrackMaker
{
	public class LevelInfo
	{
		public int nPaperInitX;
		public int nPaperInitY;
		public int nBinBottomY;
		public int nBinTopY;
		public int nBinCenterX;
		public int nBinWidth;
		public int nUpFrameNum;
		public int nDownFrameNum;
		public int nDropFrameNum;
		public int nBackFrameNum;
		public int nFlyRight;
		public int nFlyTop;
		public int nFlyFloor;
		public int nFlyBackTop;
		public int nFlyBackFromFloor;
		public float rInitScale;
		public float rTopScale;
		public float rLastScale;
		
		public int nStartAngle;	
		public int nEndAngle;	

		public LevelInfo()
		{
		}

		public LevelInfo(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10, 
						 int p11, int p12, int p13, int p14, float p15, float p16, float p17, int p18, int p19, int p20)
		{
			nPaperInitX = p1; nPaperInitY = p2; nBinBottomY = p3;
			nBinTopY = p4; nBinCenterX = p5; nBinWidth = p6;
			nUpFrameNum = p7; nDownFrameNum = p8; nDropFrameNum = p9;
			nBackFrameNum = p10; nFlyRight = p11; nFlyTop = p12;
			nFlyBackTop = p13; nFlyBackFromFloor = p14; rInitScale = p15;
			rTopScale = p16; rLastScale = p17; nStartAngle = p18;
			nEndAngle = p19; nFlyFloor = p20;
		}
	}
	// variable
	public final int UP_FRAME_NUM = 22;
	private final int DOWN_FRAME_NUM = 27;
	private final int DROP_FRAME_NUM = 5;

	private final int BACK_FRAME_NUM = 20;
	private final int BACK_HALF_FRAME_NUM = 9;

	private final int RING_OUT_FRAME_NUM = 22;
	private final int TOP_FRAME_NUM = 4;
	private final int BACK_FROM_FLOOR_FRAME_NUM = 4;
	private final int STOP_FRAME_NUM = 16;

	private final int TOTAL_FRAME_NUM = UP_FRAME_NUM + DOWN_FRAME_NUM  + DROP_FRAME_NUM + RING_OUT_FRAME_NUM + TOP_FRAME_NUM + STOP_FRAME_NUM;

	//private final int CAMERA_WIDTH = 320;
	//private final int CAMERA_HEIGHT = 480;

	private final int IPHONE_ROUND_LIMIT = 8;

	private final int UPTIME = 1;
	private final int DOWNTIME = 1;
	private final float DROPTIME = 0.3f;
	private final float BACKTIME = 0.3f;
	private final int MAX_WIND = 6;

	private final float PI = 3.14f;
	
	private int[] m_nTrackX;
	private int[] m_nTrackY;
	private float[] m_rScale;
	private int[] m_nState;
	private int[] m_nRound;
	
	private boolean m_fSuccess;
	private int m_nWindDrect;
	private float m_rWindSpeed;
	private float m_rThrowAngle;
	private int m_nRealFrameNum;
	private int m_nCurrentFrameIndex;
	private int m_nCurrentRoundIndex;

	/* kcl insert */
	private float m_rScaleX, m_rScaleY;
	private LevelInfo[] levelInfo;
	public LevelInfo m_levelInfo;

	private final int LV_NUM = 8;

	private final int STATE_NONE = 0;
	private final int STATE_IN = 1;
	private final int STATE_SIDE_IN = 2;
	private final int STATE_RING_IN = 3;
	private final int STATE_RING_OUT = 4;
	private final int STATE_OUT = 5;

	// method
	public TrackMaker(float rScaleX, float rScaleY) 
	{
		m_nCurrentFrameIndex = 0;
		m_rScaleX = rScaleX; m_rScaleY = rScaleY;
		setWindSpeed1(1, 0);
		initLevelInfo(); // kcl insert
		arrayAlloc();
	}

	public void setWindSpeed1(int nDirect, float rSpeed)
	{
		m_nWindDrect = nDirect;
		m_rWindSpeed = rSpeed;
	}
	
	private void arrayAlloc()
	{
		m_nTrackX = new int[TOTAL_FRAME_NUM];
		m_nTrackY = new int[TOTAL_FRAME_NUM];
		m_rScale = new float[TOTAL_FRAME_NUM];
		m_nState = new int[TOTAL_FRAME_NUM];
		m_nRound = new int[TOTAL_FRAME_NUM];
	}

	private void initLevelInfo()
	{
		levelInfo = new LevelInfo [LV_NUM];
		
		if (Global.CAMERA_WIDTH == 320.0f) {
			levelInfo[0] = new LevelInfo(158, 430, 241 + 31, 241, 96 + 126 / 2, 126 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 90, 206, 5, 1, 0.7f, 0.2f, 40, 140, 272);
			levelInfo[1] = new LevelInfo(158, 430, 365/*223 + 43*/, 206, 138 + 45 / 2, 45 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 80, 188, 5, 1, 0.7f, 0.2f, 40, 140, 268);
			levelInfo[2] = new LevelInfo(158, 430, 253/*131 + 27*/, 131, 148 + 25 / 2, 25 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 50, 106, 5, 1, 0.7f, 0.1f, 50, 130, 158);
		} else {
			levelInfo[0] = new LevelInfo(240, 714, 447, 400, 147 + 187 / 2, 187 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 520, 160, 350, 10, 1, 0.7f, 0.2f, 40, 140, 447);
			levelInfo[1] = new LevelInfo(240, 714, 600, 346, 206 + 68 / 2, 68 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 520, 140, 301, 5, 1, 0.7f, 0.2f, 40, 140, 412);
			levelInfo[2] = new LevelInfo(240, 714, 427, 237, 222 + 38 / 2, 38 / 2, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 520, 110, 202, 5, 1, 0.7f, 0.1f, 50, 130, 263);
		}

//		levelInfo[3] = new LevelInfo(160, 460, 332, 285, 160, 27, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 140, 238, 5, 1, 0.7f, 0.25f, 40, 140);
		levelInfo[3] = new LevelInfo(160, 460, 230, 201, 162, 30, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 40, 200, 5, 1, 0.7f, 0.25f, 40, 140, 230);
//		levelInfo[4] = new LevelInfo(161, 460, 273, 243, 161, 16, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 120, 200, 5, 1, 0.7f, 0.12f, 50, 130);
		levelInfo[4] = new LevelInfo(161, 460, 328, 280, 160, 33, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 100, 262, 5, 1, 0.7f, 0.12f, 50, 130, 328);
//		levelInfo[5] = new LevelInfo(160, 460, 272, 254, 160, 10, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 110, 236, 3, 1, 0.7f, 0.1f, 50, 130);
		levelInfo[5] = new LevelInfo(160, 460, 346, 280, 160, 40, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 120, 260, 3, 0.8f, 0.6f, 0.25f, 50, 130, 346);
//		levelInfo[6] = new LevelInfo(159, 460, 254, 211, 159, 19, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 55, 168, 3, 1, 0.7f, 0.25f, 50, 130);
		levelInfo[6] = new LevelInfo(159, 460, 227, 188, 163, 26, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 110, 170, 3, 1, 0.7f, 0.25f, 50, 130, 227);
//		levelInfo[7] = new LevelInfo(160, 460, 327, 308, 160, 9, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 180, 289, 3, 1, 0.7f, 0.09f, 50, 130);
		levelInfo[7] = new LevelInfo(160, 460, 330, 308, 160, 16, UP_FRAME_NUM, DOWN_FRAME_NUM, DROP_FRAME_NUM, BACK_FRAME_NUM, 350, 200, 289, 3, 1, 0.7f, 0.09f, 50, 130, 330);

		m_levelInfo = new LevelInfo();
	}

	public void setLevel(int nLevelNum)
	{
		m_levelInfo.nBackFrameNum = BACK_FRAME_NUM;
		m_levelInfo.nBinBottomY = (int)(levelInfo[nLevelNum].nBinBottomY * m_rScaleY);
		m_levelInfo.nBinCenterX = (int)(levelInfo[nLevelNum].nBinCenterX * m_rScaleX);
		m_levelInfo.nBinTopY = (int)(levelInfo[nLevelNum].nBinTopY * m_rScaleY);
		m_levelInfo.nBinWidth = (int)(levelInfo[nLevelNum].nBinWidth * m_rScaleX);
		m_levelInfo.nDownFrameNum = DOWN_FRAME_NUM;
		m_levelInfo.nDropFrameNum = DROP_FRAME_NUM;
		m_levelInfo.nFlyBackTop = (int)(levelInfo[nLevelNum].nFlyBackTop * m_rScaleY);
		m_levelInfo.nFlyTop = (int)(levelInfo[nLevelNum].nFlyTop * m_rScaleY);
		m_levelInfo.nPaperInitX = (int)(levelInfo[nLevelNum].nPaperInitX * m_rScaleX);
		m_levelInfo.nPaperInitY = (int)(levelInfo[nLevelNum].nPaperInitY * m_rScaleY);
		m_levelInfo.nUpFrameNum = UP_FRAME_NUM;
		m_levelInfo.rInitScale = levelInfo[nLevelNum].rInitScale;
		m_levelInfo.rTopScale = levelInfo[nLevelNum].rTopScale;
		m_levelInfo.rLastScale = levelInfo[nLevelNum].rLastScale;
		m_levelInfo.nFlyBackFromFloor = (int)(levelInfo[nLevelNum].nFlyBackFromFloor * m_rScaleY);
		m_levelInfo.nFlyRight = (int)(levelInfo[nLevelNum].nFlyRight * m_rScaleX);
		m_levelInfo.nStartAngle = levelInfo[nLevelNum].nStartAngle;
		m_levelInfo.nEndAngle = levelInfo[nLevelNum].nEndAngle;
		m_levelInfo.nFlyFloor = (int)(levelInfo[nLevelNum].nFlyFloor * m_rScaleY);
	}

	public LevelInfo getLevelInfo()
	{
		return m_levelInfo;
	}

	public int setThrowAngle(float rThrowAngle)
	{
		m_rThrowAngle = rThrowAngle;
		
		int nAngle = (int)(m_rThrowAngle * 180 / PI);
		if (nAngle < m_levelInfo.nStartAngle || nAngle > m_levelInfo.nEndAngle)
			return 0;
		calcTrack();
		return 1;
	}

	private void calcTrack()
	{
		float rInterVal;
		m_nCurrentFrameIndex = 0;
		m_nRealFrameNum = 0;
		m_fSuccess = false;
		rInterVal = (float)UPTIME / UP_FRAME_NUM;
		int nFrameIndex;
		float rUpAy, rDownAy, rAx;
		float rV0y, rV0x;
		float rLastV0x = 0;
		int nPaperX = 0, nPaperY = 0;
		int nLastPaperX = 0, nLastPaperY = 0;
		float rLastScale = 0;
		float rScale;
		int[] x = {0}, y = {0};
		float[] scale = {0.0f};
		
		rUpAy = (float)2 * (m_levelInfo.nPaperInitY - m_levelInfo.nFlyTop) / sqr(UPTIME);
		rDownAy = (float)2 * (m_levelInfo.nBinBottomY - m_levelInfo.nFlyTop) / sqr(DOWNTIME);
		rV0y = rUpAy * UPTIME;
		rAx = (float)(m_levelInfo.nFlyRight - m_levelInfo.nBinCenterX) * 2 * m_rWindSpeed * m_nWindDrect / (MAX_WIND);
		rV0x = rV0y * (float)(Math.cos(m_rThrowAngle)/ Math.sin(m_rThrowAngle));
		rScale = m_levelInfo.rInitScale;
		m_nCurrentRoundIndex = -1;
		
		for (nFrameIndex = 0; nFrameIndex < UP_FRAME_NUM; nFrameIndex++)
		{
			int nDeltaX, nDeltaY;
			float rCurrentTime;
			
			rCurrentTime = rInterVal * nFrameIndex;
			nDeltaY = (int)(rV0y * rCurrentTime - rUpAy * sqr(rCurrentTime) / 2);
			nPaperY = (int)(m_levelInfo.nPaperInitY - nDeltaY);
			if (rV0x * m_nWindDrect > 0)
			{
				nDeltaX = (int)(rV0x * rCurrentTime + rAx * 0.3 * sqr(rCurrentTime) / 2);
			}
			else
			{
				nDeltaX = (int)(nDeltaY * Math.cos(m_rThrowAngle)/ Math.sin(m_rThrowAngle));
			}
			
			nPaperX = m_levelInfo.nPaperInitX + nDeltaX;
			if (m_nCurrentFrameIndex != 18 && m_nCurrentFrameIndex != 20)
			{
				addRoundIndex();
			}
			
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
			m_rScale[m_nCurrentFrameIndex] = rScale;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			nLastPaperX = nPaperX;
			nLastPaperY = nPaperY;
			rLastScale = rScale;
			rScale -= (m_levelInfo.rInitScale - m_levelInfo.rTopScale) / UP_FRAME_NUM;
			m_nCurrentFrameIndex++;
			rLastV0x = rV0x;
		}

		for (nFrameIndex = 0; nFrameIndex < TOP_FRAME_NUM; nFrameIndex++)
		{
			if (rV0x * m_nWindDrect > 0)
			{
				nPaperX += (int)((rV0x * rInterVal + rAx * sqr(rInterVal) / 2));
			}
			if (((m_nCurrentFrameIndex == 22) || (m_nCurrentFrameIndex == 24)) && (rV0x * m_nWindDrect < 0))
			{
				rScale -= (m_levelInfo.rInitScale - m_levelInfo.rTopScale) / UP_FRAME_NUM;
			}
			if ((m_nCurrentFrameIndex == 23) || ((m_nCurrentFrameIndex == 25)))
			{
				addRoundIndex();
			}
				
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
			m_rScale[m_nCurrentFrameIndex] = rScale;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			rLastScale = rScale;
			nLastPaperX = nPaperX;
			nLastPaperY = nPaperY;
			rLastV0x += rAx/* * 0.4*/ * rInterVal;
			m_nCurrentFrameIndex++;

		}

		rInterVal = (float)DOWNTIME / DOWN_FRAME_NUM;

		for (nFrameIndex = 0; nFrameIndex < DOWN_FRAME_NUM; nFrameIndex++)
		{
			int nDeltaX, nDeltaY;
			float rCurrentTime;
			
			rCurrentTime = rInterVal * nFrameIndex;

			nDeltaY = (int)(rDownAy * sqr(rCurrentTime) / 2);
			nPaperY = nLastPaperY + nDeltaY;

			if (rAx == 0)
			{
				nDeltaX = (int)(nDeltaY * Math.cos(m_rThrowAngle)/ Math.sin(m_rThrowAngle));
			}
			else
			{
				nDeltaX = (int)( rAx * sqr(rCurrentTime) / 2);
				if (rV0x * m_nWindDrect > 0)
				{
					nDeltaX += (int)(rLastV0x * rCurrentTime);
				}
			}
			nPaperX = nLastPaperX + nDeltaX;
			rScale -= (rLastScale - m_levelInfo.rLastScale) / DOWN_FRAME_NUM;

			if (m_nCurrentFrameIndex != 28 && m_nCurrentFrameIndex != 30)
			{
				addRoundIndex();
			}

			m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
			m_rScale[m_nCurrentFrameIndex] = rScale;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;
			
			if ((nPaperY >= m_levelInfo.nBinTopY)&&(m_nTrackY[m_nCurrentFrameIndex - 2] <m_levelInfo.nBinTopY))
			{
				int nPassBinPaperX, nPassedX, nPassedY;
				nPassedX = m_nTrackX[m_nCurrentFrameIndex - 2];
				nPassedY = m_nTrackY[m_nCurrentFrameIndex - 2];
				nPassBinPaperX = ((m_levelInfo.nBinTopY- nPassedY)* (nPaperX - nPassedX))/(nPaperY - nPassedY) + nPassedX;

				if ((abs(nPassBinPaperX - m_levelInfo.nBinCenterX) < m_levelInfo.nBinWidth * 0.8))
				{
					if ((abs(nPassBinPaperX - m_levelInfo.nBinCenterX) < m_levelInfo.nBinWidth * 0.5f))
					{
						m_nState[m_nCurrentFrameIndex - 1] = STATE_NONE;
					}
					else
					{
						m_nState[m_nCurrentFrameIndex - 1] = STATE_SIDE_IN;
					}
					m_nTrackY[m_nCurrentFrameIndex -1] = m_levelInfo.nBinTopY;
					m_nTrackX[m_nCurrentFrameIndex -1] = nPassBinPaperX;
					nPaperY = m_levelInfo.nBinTopY;
					nPaperX = nPassBinPaperX;
					
					x[0] = nPaperX; y[0] = nPaperY; scale[0] = rScale;
					calcDropTrack(x, y, scale);
					nPaperX = x[0]; nPaperY = y[0]; rScale = scale[0];

					m_fSuccess = true;
					break;
				}
				if ((abs(nPassBinPaperX - m_levelInfo.nBinCenterX) >= m_levelInfo.nBinWidth * 0.8)&&
					(abs(nPassBinPaperX - m_levelInfo.nBinCenterX) < m_levelInfo.nBinWidth  * 1.05f)) 
				{
					int nMode;
					if (nPaperX < m_levelInfo.nBinCenterX)
					{
						nMode = 1;
					}
					else
					{
						nMode = -1;
					}
					//m_nCurrentFrameIndex--;
					m_nTrackY[m_nCurrentFrameIndex -1] = m_levelInfo.nBinTopY;
					m_nState[m_nCurrentFrameIndex -1] = STATE_RING_IN;
					m_nTrackX[m_nCurrentFrameIndex -1] = nPassBinPaperX;
					nPaperY = m_levelInfo.nBinTopY;
					nPaperX = nPassBinPaperX;
					x[0] = nPaperX; y[0] = nPaperY; scale[0] = rScale;
					calcBackTrack(x, y, scale, nMode);
					nPaperX = x[0]; nPaperY = y[0]; rScale = scale[0];
					m_fSuccess = true;
					
					break;
				}
				if ((((abs(nPassBinPaperX - m_levelInfo.nBinCenterX) >= m_levelInfo.nBinWidth  * 1.05f)&&
					(abs(nPassBinPaperX - m_levelInfo.nBinCenterX) < m_levelInfo.nBinWidth  * 1.15f))))
				{
					int nMode;
					if (nPaperX < m_levelInfo.nBinCenterX)
					{
						nMode = -1;
					}
					else
					{
						nMode = 1;
					}
					//m_nCurrentFrameIndex--;
					m_nTrackY[m_nCurrentFrameIndex -1] = m_levelInfo.nBinTopY;
					m_nState[m_nCurrentFrameIndex -1] = STATE_RING_OUT;
					m_nTrackX[m_nCurrentFrameIndex -1] = nPassBinPaperX;
					nPaperY = m_levelInfo.nBinTopY;
					nPaperX = nPassBinPaperX;
					x[0] = nPaperX; y[0] = nPaperY; scale[0] = rScale;
					calcRingOutTrack(x, y, scale, nMode);
					nPaperX = x[0]; nPaperY = y[0]; rScale = scale[0];
					break;
				}
			}
		}
		
		if (!m_fSuccess)
		{
			m_nState[m_nCurrentFrameIndex - 1] = STATE_OUT;
			nPaperY = m_levelInfo.nBinBottomY;
			for (nFrameIndex = 0; nFrameIndex < BACK_FROM_FLOOR_FRAME_NUM; nFrameIndex++)
			{
				if (nFrameIndex == 0)
				{
					nPaperY -= (int)(10 * m_levelInfo.rLastScale);
					nPaperX += (int)(rAx * 0.02 * m_levelInfo.rLastScale);
				}
				else if ((nFrameIndex >=2)&&(nFrameIndex < BACK_FROM_FLOOR_FRAME_NUM))
				{
					nPaperY += (int)(7 * m_levelInfo.rLastScale);
					nPaperX += (int)(rAx * 0.01* m_levelInfo.rLastScale);
				}
				
				m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
				m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
				m_rScale[m_nCurrentFrameIndex] = rScale;
				m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
				m_nState[m_nCurrentFrameIndex] = STATE_NONE;
				m_nCurrentFrameIndex++;
			}
		}
		for (nFrameIndex = 0; nFrameIndex < STOP_FRAME_NUM; nFrameIndex++)
		{
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX;
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY;
			m_rScale[m_nCurrentFrameIndex] = rScale;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;
		}
		
		m_nRealFrameNum = m_nCurrentFrameIndex;
		return;
	}

	private float sqr( float x )
	{
		return x * x;
	}

	private int abs( int nVal )
	{
		return nVal < 0 ? -(nVal) : nVal;
	}

	private void calcBackTrack(int[] nPaperX, int[] nPaperY, float[] rScale, int nMode)
	{
		float rAyUp = (float)(2 * (m_levelInfo.nBinTopY - m_levelInfo.nFlyBackTop) / sqr(BACKTIME / 2));
		float rVY0 = (float)(rAyUp * BACKTIME / 2);
		int nDeltaY;
		float rInterVal = (float)(BACKTIME / 2) / BACK_HALF_FRAME_NUM;
		
		int nStartX = nPaperX[0];
		int nFrameIndex;
		
		for (nFrameIndex = 0; nFrameIndex < BACK_HALF_FRAME_NUM; nFrameIndex++)
		{
			float rCurrentTime = (nFrameIndex + 1) * rInterVal;
			nPaperX[0] = nStartX + nMode * (m_levelInfo.nBinWidth * (nFrameIndex + 1) / 2 / BACK_HALF_FRAME_NUM);
			nDeltaY = (int)(rVY0 * rCurrentTime - rAyUp * sqr(rCurrentTime) / 2);
			nPaperY[0] = (int)(m_levelInfo.nBinTopY - nDeltaY);
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX[0];
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY[0];
			m_rScale[m_nCurrentFrameIndex] = rScale[0];
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;
		}

		int nDownFrameNum = BACK_FRAME_NUM - BACK_HALF_FRAME_NUM;
		int nInBinFrameNum = nDownFrameNum - BACK_HALF_FRAME_NUM;

		float rScaleInterval = (rScale[0] - m_levelInfo.rLastScale) / nInBinFrameNum;
		nStartX = nPaperX[0];
		
		for (nFrameIndex = 0; nFrameIndex < nDownFrameNum; nFrameIndex++)
		{
			float rCurrentTime = (nFrameIndex + 1) * rInterVal;
			nDeltaY = (int)(rAyUp * sqr(rCurrentTime) / 2);
			nPaperY[0] = m_levelInfo.nFlyBackTop + nDeltaY;

			if (nFrameIndex < BACK_HALF_FRAME_NUM)
			{
				nPaperX[0] = nStartX + nMode * (m_levelInfo.nBinWidth * (nFrameIndex + 1) / 2 / nDownFrameNum);
			}
			else
			{
				rScale[0] -= rScaleInterval;
			}
			
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX[0];
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY[0];
			m_rScale[m_nCurrentFrameIndex] = rScale[0];
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;
		}
		nPaperX[0] = m_levelInfo.nBinCenterX;
		calcDropTrack(nPaperX, nPaperY, rScale);
	}

	private void calcDropTrack(int[] nPaperX, int[] nPaperY, float[] rScale)
	{
		float rDropSpeedX = (float)((m_levelInfo.nBinCenterX - nPaperX[0]) / DROPTIME);
		float rDropSpeedY = (float)(((m_levelInfo.nBinBottomY - (m_levelInfo.nBinBottomY - m_levelInfo.nBinTopY) * 0.83) - nPaperY[0]) / DROPTIME);
		int nFrameIndex;
		float rInterVal = (float)DROPTIME / DROP_FRAME_NUM;
		float rInitScale = rScale[0];
		int nStartX, nStartY;
		nStartX = nPaperX[0];
		nStartY = nPaperY[0];
		for (nFrameIndex = 0; nFrameIndex < DROP_FRAME_NUM; nFrameIndex++)
		{
			nPaperX[0] = nStartX + (int)((rInterVal * nFrameIndex) * rDropSpeedX);
			nPaperY[0] = nStartY + (int)((rInterVal * nFrameIndex) * rDropSpeedY);
			rScale[0] -= (rInitScale - m_levelInfo.rLastScale) / DROP_FRAME_NUM;
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX[0];
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY[0];
			m_rScale[m_nCurrentFrameIndex] = rScale[0];
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			if (nFrameIndex == 0)
				m_nState[m_nCurrentFrameIndex] = STATE_IN;
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nCurrentFrameIndex++;
		}
	}

	private void calcRingOutTrack(int[] nPaperX, int[] nPaperY, float[] rScale, int nMode)
	{
		float rAyUp = (float)(2 * (m_levelInfo.nBinTopY - m_levelInfo.nFlyBackTop) / sqr(BACKTIME / 2));
		float rVY0 = (float)(rAyUp * BACKTIME / 2);
		int nDeltaY, nDeltaX;
		int nInitX = nPaperX[0];
		float rInitScale = rScale[0];

		float rInterVal = (float)(BACKTIME  / 2)/ BACK_HALF_FRAME_NUM;
		
		for (int nFrameIndex = 0; nFrameIndex < RING_OUT_FRAME_NUM; nFrameIndex++)
		{
			float rCurrentTime = (nFrameIndex + 1) * rInterVal;
			nDeltaX = (int)(nMode * (m_levelInfo.nBinWidth / BACKTIME) * rCurrentTime);
			nPaperX[0] = nInitX + nDeltaX;
			nDeltaY = (int)(rVY0 * rCurrentTime - rAyUp * sqr(rCurrentTime) / 2);
			nPaperY[0] = m_levelInfo.nBinTopY - nDeltaY;
			if (nFrameIndex > BACKTIME / 2)
			{
				rScale[0] -= (rInitScale - m_levelInfo.rLastScale) / (RING_OUT_FRAME_NUM - BACK_HALF_FRAME_NUM);
			}
			m_nTrackX[m_nCurrentFrameIndex] = nPaperX[0];
			m_nTrackY[m_nCurrentFrameIndex] = nPaperY[0];
			m_rScale[m_nCurrentFrameIndex] = rScale[0];
			m_nRound[m_nCurrentFrameIndex] = m_nCurrentRoundIndex;
			m_nState[m_nCurrentFrameIndex] = STATE_NONE;
			m_nCurrentFrameIndex++;
			if (nPaperY[0] > m_levelInfo.nBinBottomY)
			{
				nPaperY[0] = m_levelInfo.nBinBottomY;
				m_nTrackY[m_nCurrentFrameIndex - 1] = nPaperY[0];
				break;
			}
		}
	}

	public boolean getTrack(int nFrameNum, int[] nPaperX, int[] nPaperY, float[] rScale, int[] nRotate, int[] nState)
	{
		if (nFrameNum >= m_nRealFrameNum)
		{
			return false;
		}
		nRotate[0] = 1;
		nPaperX[0] = m_nTrackX[nFrameNum];
		nPaperY[0] = m_nTrackY[nFrameNum];
		rScale[0] = m_rScale[nFrameNum];
		nState[0] = m_nState[nFrameNum];
		nRotate[0] = m_nRound[nFrameNum];

		return true;
	}

	public void setWindSpeed(int nDirect, float rSpeed)
	{
		m_nWindDrect = nDirect;
		m_rWindSpeed = rSpeed;
	}

	public int getRealFrameNum()
	{
		return m_nRealFrameNum;
	}

	public boolean getSuccess()
	{
		return m_fSuccess;
	}

	public void addRoundIndex()
	{
		int nRoundLimit;

		nRoundLimit = IPHONE_ROUND_LIMIT;// ??
		
		m_nCurrentRoundIndex++;
		if (m_nCurrentRoundIndex == nRoundLimit)
		{
			m_nCurrentRoundIndex = 0;
		}
	}
}
