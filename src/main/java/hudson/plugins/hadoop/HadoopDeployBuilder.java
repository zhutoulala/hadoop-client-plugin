package hudson.plugins.hadoop;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.core.Response;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;

public class HadoopDeployBuilder extends Builder implements Serializable {
	private final String artifact;
	
	@DataBoundConstructor
	HadoopDeployBuilder(String artifact) {
		this.artifact = artifact;
	}
	
	public String getArtifact() {
		return artifact;
	}
	@Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        
		HDFSDeployer hdfsDeployer = new HDFSDeployer();

        listener.getLogger().println("Deploying artifact: " + artifact);
        hdfsDeployer.deploy(artifact, getDescriptor().getHDFSURI());
        return true;
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return (DescriptorImpl)super.getDescriptor();
    }

    // this annotation tells Hudson that this is the implementation of an extension point
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

    	private String hdfsURI;
    	
        public DescriptorImpl() {
            load();
        }
    	
        @Override
        public String getDisplayName() {
            return Messages.Hadoop_BuilderName();
        }

        @Override
        public boolean isApplicable(Class type) {
            return true;
        }
        
        @Override
        public boolean configure(StaplerRequest staplerRequest, JSONObject json) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
        	hdfsURI = json.getBoolean("HDFSURI");
            save();
            return true; // indicate that everything is good so far
        }

        public String getHDFSURI() {
            return hdfsURI;
        }
    }

}
