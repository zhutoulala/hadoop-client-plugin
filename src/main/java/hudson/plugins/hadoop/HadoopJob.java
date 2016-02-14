package hudson.plugins.hadoop;

public class HadoopJob {
	
	private String jobHDFSPath;
	private JobType jobType;

	public enum JobType {
		MAPREDUCE,
		HIVE,
		PIG,
		HBASE,
		OOZIE,
	}
	
	
	
	public String getJobHDFSPath() {
		return jobHDFSPath;
	}

	public void setJobHDFSPath(String jobHDFSPath) {
		this.jobHDFSPath = jobHDFSPath;
	}

	public JobType getJobType() {
		return jobType;
	}

	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}

	public HadoopJob(String jobHDFSPath, JobType jobType) {
		this.jobHDFSPath = jobHDFSPath;
		this.jobType = jobType;
	}
	
	boolean run(String jobLaunchNode) {
		//TODO 
		return true;
	}
}
