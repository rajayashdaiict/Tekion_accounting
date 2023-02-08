package com.Cricket.com.cricketProject;

public class GamePlay {
    static void gamePlay(Match match){

        match.startMsg();

//        Scanner sc = new Scanner(System.in);
//        System.out.println("Enter the name of the team1:");
//        String temp = sc.nextLine();
        String temp="Yash";
        Team team1=new Team(temp);
//        System.out.println("Enter the name of the team2:");
//        temp = sc.nextLine();
        temp="Yash1";
        Team team2=new Team(temp);
//        System.out.println("Enter the overs of the match:");
//        match.overs =sc.nextInt();
        match.overs = 10;


        Pair<Team, Team> teams;
        teams = new Pair<>(team1,team2);

        teams  =  match.toss(teams);

        Inning inning1 = new Inning(teams,match.overs);
        ScoreCard inning1ScoreCard = new ScoreCard(inning1);
        inning1.setScoreCard(inning1ScoreCard);
        inning1.play();

        teams=match.swapTeams(teams);

        Inning inning2 = new Inning(teams,match.overs,inning1ScoreCard.totalRuns);
        ScoreCard inning2ScoreCard = new ScoreCard(inning2);
        inning2.setScoreCard(inning2ScoreCard);
        inning2.play();

        System.out.println("Team "+inning1.battingTeam.name+" has made "+inning1ScoreCard.totalRuns+" and "+"Team "+inning2.battingTeam.name+" has made "+inning2ScoreCard.totalRuns);
        match.winner = match.decideWinner(inning1ScoreCard , inning2ScoreCard);

        System.out.println(match.winner.name+" has won the game");
        Pair<ScoreCard,ScoreCard> matchResults;
        matchResults = new Pair<>(inning1ScoreCard,inning2ScoreCard);
        CricketController restController = new CricketController();
        restController.setMatchResults(matchResults);
    }
}
