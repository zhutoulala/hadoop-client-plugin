package hudson.plugins.hadoop;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;

public class HDFSDeployPublisher extends Publisher implements Serializable {
	private final String artifactToDeploy;
	
	@DataBoundConstructor
	public HDFSDeployPublisher(String artifactToDeploy) {
		this.artifactToDeploy = artifactToDeploy;
	}
	
	public String getArtifactToDeploy() {
		return artifactToDeploy;
	}
	
	@Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        
		HDFSDeployer hdfsDeployer = new HDFSDeployer();

        listener.getLogger().println("Deploying artifact: " + artifactToDeploy);
        try {
			hdfsDeployer.deploy(artifactToDeploy, getDescriptor().getHDFSURI(),listener);
		} catch (URISyntaxException e) {
			listener.error(e.getMessage());
			return false;
		}
        return true;
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return (DescriptorImpl)super.getDescriptor();
    }

    
    public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return null;
	}
    
    // this annotation tells Hudson that this is the implementation of an extension point
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

    	private String hdfsURI;
    	
        public DescriptorImpl() {
            load();
        }
    	
        @Override
        public String getDisplayName() {
            return Messages.HDFSDeployPublisherName();
        }

        @Override
        public boolean isApplicable(Class type) {
            return true;
        }
        
        @Override
        public boolean configure(StaplerRequest staplerRequest, JSONObject json) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
        	hdfsURI = json.getString("HDFSURI");
            save();
            return true; // indicate that everything is good so far
        }

        public String getHDFSURI() {
            return hdfsURI;
        }
    }

}
