Type SwarmData
	Field alienType
	Field numOfAliens
	Field numOfRows
	Field speed
	Field xVar
	Field yVar
	Field xSpacer
	Field ySpacer
	Field flightTime
	Field displayTime
End Type

Function CreateSwarm(aType,numAliens,numRows,speed,x,y,xSpacer,ySpacer,fltTime,dTime)

	s.SwarmData = New SwarmData

	s\alienType = aType
	s\numOfAliens = numAliens
	s\numOfRows = numRows
	s\speed = speed
	s\xVar = x
	s\yVar = y
	s\xSpacer = xSpacer
	s\ySpacer = ySpacer
	s\flightTime = fltTime
	s\displayTime = dTime * ONESECOND
	
	aliensTotal = aliensTotal + numAliens
	
End Function

Function UpdateSwarms()

	numberOfSwarms = 0

	For s.SwarmData = Each SwarmData
		If s\displayTime + missionStartTime <= currentGameTime Then
			Select s\alienType
			
				Case STD_ALIEN
					StandardSwarm(s)
					
				Case BOMBER_ALIEN
					BomberSwarm(s)
					
				Case ACE_ALIEN
					AceSwarm(s)
		
			End Select
		
			Delete s
		EndIf
		numberOfSwarms = numberOfSwarms + 1
	Next
	
	Return numberOfSwarms

End Function

Function StandardSwarm(s.SwarmData)

	flightTime = s\flightTime + currentGameTime
	startX = s\xVar
	startY = s\yVar

	y = 0 - stdAlien\height
			
	For i = 1 To s\numOfAliens
	CreateStandardAlien(startX,y,s\speed,flightTime,stdAlien\startFrame,stdAlien\image,startX,startY)
		startX = Max(startX + (s\xSpacer + stdAlien\width), WIDTH - stdAlien\width)
		If Not (i Mod s\numOfRows) Then
			startX = s\xVar
			startY = Max(startY + (s\ySpacer + stdAlien\height), heightIncHUD - stdAlien\height)
		EndIf
	Next
	
End Function

Function BomberSwarm(s.SwarmData)

	flightTime = s\flightTime + currentGameTime
	startX = s\xVar
	startY = s\yVar
	
	y = 0 - bombAlien\height
			
	For i = 1 To s\numOfAliens
	CreateBomberAlien(startX,y,s\speed,flightTime,bombAlien\startFrame,bombAlien\image,startX,startY)
		startX = Max(startX + (s\xSpacer + bombAlien\width), WIDTH - bombAlien\width)
		If Not (i Mod s\numOfRows) Then
			startX = s\xVar
			startY = Max(startY + (s\ySpacer + bombAlien\height), heightIncHUD - bombAlien\height)
		EndIf
	Next

End Function

Function AceSwarm(s.SwarmData)

	flightTime = s\flightTime + currentGameTime
	startX = s\xVar
	startY = s\yVar

	y = 0 - aceAlien\height
			
	For i = 1 To s\numOfAliens
	CreateAceAlien(startX,y,s\speed,flightTime,aceAlien\startFrame,aceAlien\image,startX,startY)
		startX = Max(startX + (s\xSpacer + aceAlien\width), WIDTH - aceAlien\width)
		If Not (i Mod s\numOfRows) Then
			startX = s\xVar
			startY = Max(startY + (s\ySpacer + aceAlien\height), heightIncHUD - aceAlien\height)
		EndIf
	Next

End Function