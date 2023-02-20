# Tekion_accounting
To share learning progress with the team and mentor


apis
<br />
<img width="301" alt="image" src="https://user-images.githubusercontent.com/54074117/219371997-c444c2b7-0fd6-427f-974f-1d877e8b1a5c.png">

<mark>1.endpoint : /addplayer</mark> : add player into database, not associated with team initially

Request body:
{
    "name" : ""
}

Responce body:
Success or error in string format 

<mark>2.endpoint : /addteam 
</mark> : add team into database with 0 players 

Request body:
{
    "name":""
}

Responce body:
Success or error in string format

<mark>3.endpoint : /addplayerintoteam</mark> : associate player and team, player and team should already be created

Request body:
{
    "playerId" : int,
    "teamId" : int
}

Responce body:
Success or error in string format

<mark>4.endpoint : /play</mark> : simulate whole match between two already formed teams and give winner team as result, whole stats of match would have been store in database also

Request body:
{
    "team1Id":int,
    "team2Id":int,
    "overs":int
}

Responce body:
{
    "winnerTeamName":""
}



