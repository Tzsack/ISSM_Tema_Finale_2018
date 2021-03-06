/* Generated by AN DISI Unibo */ 
package it.unibo.qa2frontend;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractQa2frontend extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractQa2frontend(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/qa2frontend/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/qa2frontend/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitForEvents",waitForEvents);
	    	stateTab.put("emitMqttEvent",emitMqttEvent);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "qa2frontend tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	//ConnectToPublish
	    	connectToSend( this.getName(), "tcp://localhost:1883", "qa2frontend");
	    	temporaryStr = "\"qa2frontend STARTED\"";
	    	println( temporaryStr );  
	    	//switchTo waitForEvents
	        switchToPlanAsNextState(pr, myselfName, "qa2frontend_"+myselfName, 
	              "waitForEvents",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitForEvents = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitForEvents",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitForEvents";  
	    	//bbb
	     msgTransition( pr,myselfName,"qa2frontend_"+myselfName,false,
	          new StateFun[]{stateTab.get("emitMqttEvent") }, 
	          new String[]{"true","E","modelChanged" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitForEvents){  
	    	 println( getName() + " plan=waitForEvents WARNING:" + e_waitForEvents.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitForEvents
	    
	    StateFun emitMqttEvent = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("emitMqttEvent",-1);
	    	String myselfName = "emitMqttEvent";  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("modelChanged(RES)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("modelChanged") && 
	    		pengine.unify(curT, Term.createTerm("modelChanged(RES)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg = "modelChanged(RES)";
	    			/* PublishEventMove */
	    			parg =  updateVars( Term.createTerm("modelChanged(RES)"), 
	    			                    Term.createTerm("modelChanged(RES)"), 
	    				    		  	Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsgMqtt(  "qa2frontend", "modelChanged", "none", parg );
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"qa2frontend_"+myselfName,false,true);
	    }catch(Exception e_emitMqttEvent){  
	    	 println( getName() + " plan=emitMqttEvent WARNING:" + e_emitMqttEvent.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//emitMqttEvent
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
