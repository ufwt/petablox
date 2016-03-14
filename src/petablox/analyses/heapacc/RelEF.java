package petablox.analyses.heapacc;

import petablox.analyses.heapacc.DomE;
import petablox.analyses.field.DomF;
import petablox.program.Program;
import petablox.project.Petablox;
import petablox.project.analyses.ProgramRel;
import soot.SootField;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;
import petablox.util.soot.SootUtilities;

/**
 * Relation containing each tuple (e,f) such that quad e accesses
 * (reads or writes) instance field, static field, or array element f.
 *
 * @author Mayur Naik (mhn@cs.stanford.edu)
 */
@Petablox(
    name = "EF",
    sign = "E0,F0:F0_E0"
)
public class RelEF extends ProgramRel {
    public void fill() {
    	int within=0;
    	int isload=0;
    	int out=0;
    	int notassgn=0;
        DomE domE = (DomE) doms[0];
        DomF domF = (DomF) doms[1];
        int numE = domE.size();
        for (int eIdx = 0; eIdx < numE; eIdx++) {
            Unit e = (Unit)domE.get(eIdx);
            //jq_Field f = e.getField();
            if(e instanceof JAssignStmt){
            	
            	JAssignStmt j = (JAssignStmt)e;
            	if(j.containsFieldRef()){
            		within++;
            		SootField f = j.getFieldRef().getField();
                	int fIdx = domF.indexOf(f);
                    assert (fIdx >= 0);
                    add(eIdx, fIdx);
            	}
            	else if(SootUtilities.isLoadInst(j) || SootUtilities.isStoreInst(j)){
            		isload++;
            		int fIdx = 0;//domF.indexOf(null);
                    assert (fIdx >= 0);
                    add(eIdx, fIdx);
            	}
            	else{
            		out++;
            		System.out.println("\nPRT Else part"+eIdx);
            	}
            }
           
        }
        System.out.println("\nTotal :  "+numE+"  WIthin containsfieldref: "+within+"    Isload:  "+isload+"   Else: "+out+"   Not assign: "+notassgn);
    }
}
